package com.iispl.adapter;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.iispl.entity.IncomingTransaction;
import com.iispl.enums.ChannelType;
import com.iispl.enums.SourceType;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;



/**
 * ════════════════════════════════════════════════════════════════════
 *  RtgsAdapter  —  Real-Time Gross Settlement Adapter
 * ════════════════════════════════════════════════════════════════════
 *
 * RTGS handles high-value, time-critical interbank transfers settled
 * individually (not batched). The RBI RTGS system uses a fixed-format
 * pipe-delimited message structure.
 *
 * Key RTGS characteristics reflected in the format:
 *   • Minimum amount is INR 2,00,000 (enforced in adapt())
 *   • Transactions settle in real-time — valueDate is always today
 *   • Each record carries an IFSC code for fromBank and toBank
 *   • Priority field indicates urgency (HIGH / NORMAL)
 *
 * File format  : .txt  (pipe-delimited)
 * Source system: RTGS
 * Channel used : RTGS (always)
 *
 * Column order:
 * SOURCE_REF | TXN_TYPE | AMOUNT | CURRENCY | VALUE_DATE |
 * ACCT_NO | CUST_ID | FROM_IFSC | TO_IFSC | PRIORITY
 *
 * Total: 10 columns
 *
 * Implements TransactionAdapter  —  IS-A TransactionAdapter
 */
public class RtgsAdapter implements TransactionAdapter {

    private static final String DELIMITER       = "\\|";
    private static final int    EXPECTED_COLS   = 10;
    private static final BigDecimal MIN_AMOUNT  = new BigDecimal("200000.00");
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----------------------------------------------------------------
    // TransactionAdapter contract
    // ----------------------------------------------------------------

    /**
     * Adapts a single RTGS pipe-delimited line into IncomingTransaction.
     *
     * Example line:
     *   RTGS-20240301-001|CREDIT|5000000.00|INR|2024-03-01|ACC-3001|CUST-401|SBIN0001234|HDFC0000123|HIGH
     */
    @Override
    public IncomingTransaction adapt(String rawPayload) {
        if (rawPayload == null || rawPayload.isBlank()) {
            throw new IllegalArgumentException("RtgsAdapter: rawPayload is empty.");
        }

        String[] col = rawPayload.split(DELIMITER, -1);
        if (col.length != EXPECTED_COLS) {
            throw new IllegalArgumentException(
                "RtgsAdapter: expected " + EXPECTED_COLS + " columns, got "
                + col.length + " → " + rawPayload);
        }

        BigDecimal amount = new BigDecimal(trim(col[2]));

        // RTGS minimum amount rule: INR 2,00,000
        if (amount.compareTo(MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                "RtgsAdapter: amount " + amount +
                " is below RTGS minimum of " + MIN_AMOUNT);
        }

        IncomingTransaction txn = new IncomingTransaction();

        txn.setSourceRef    (trim(col[0]));
        txn.setTxnType      (TransactionType.valueOf(trim(col[1]).toUpperCase()));
        txn.setAmount       (amount);
        txn.setCurrency     (trim(col[3]).toUpperCase());
        txn.setValueDate    (LocalDate.parse(trim(col[4]), DATE_FMT));
        txn.setAccountNumber(trim(col[5]));
        txn.setCustomerId   (trim(col[6]));
        txn.setFromBank     (trim(col[7]));   // stored as IFSC code
        txn.setToBank       (trim(col[8]));   // stored as IFSC code
        // col[9] = PRIORITY — stored in normalized payload for downstream use

        txn.setChannel      (ChannelType.RTGS);   // always RTGS for this adapter
        txn.setSourceSystem (SourceType.RTGS);
        txn.setStatus       (TransactionStatus.RECEIVED);
        txn.setRawPayload   (rawPayload);
        txn.setNormalizedPayload(buildNormalized(txn, trim(col[9])));

        return txn;
    }

    /** Reads the entire RTGS .txt file; skips the header row. */
    @Override
    public List<IncomingTransaction> adaptFile(String filePath) {
        List<IncomingTransaction> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (isHeader) { isHeader = false; continue; }
                try {
                    list.add(adapt(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("[RtgsAdapter] Skipping line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("RtgsAdapter: cannot read file: " + filePath, e);
        }

        System.out.println("[RtgsAdapter] Parsed " + list.size() + " transactions from " + filePath);
        return list;
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.RTGS;
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private String trim(String s) { return s == null ? "" : s.trim(); }

    private String buildNormalized(IncomingTransaction t, String priority) {
        return "sourceRef="     + t.getSourceRef()     + "|"
             + "txnType="       + t.getTxnType()        + "|"
             + "amount="        + t.getAmount()          + "|"
             + "currency="      + t.getCurrency()        + "|"
             + "valueDate="     + t.getValueDate()       + "|"
             + "accountNumber=" + t.getAccountNumber()   + "|"
             + "customerId="    + t.getCustomerId()      + "|"
             + "fromBank="      + t.getFromBank()        + "|"
             + "toBank="        + t.getToBank()          + "|"
             + "channel="       + t.getChannel()         + "|"
             + "sourceSystem="  + t.getSourceSystem()    + "|"
             + "priority="      + priority;
    }
}
