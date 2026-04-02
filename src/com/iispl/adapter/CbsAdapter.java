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
 *  CbsAdapter  —  Core Banking System Adapter
 * ════════════════════════════════════════════════════════════════════
 *
 * CBS sends transactions as pipe-delimited ( | ) flat text files.
 * These are internal bank ledger entries: credits, debits, intrabank
 * transfers, fee postings, and reversals of previous entries.
 *
 * File format  : .txt  (pipe-delimited)
 * Source system: CBS
 * Channel used : RTGS or NEFT (decided per record based on amount)
 *
 * Column order (first row is a header — skipped automatically):
 * ┌──────────────┬──────────┬────────────┬──────────┬────────────┐
 * │  SOURCE_REF  │ TXN_TYPE │   AMOUNT   │ CURRENCY │ VALUE_DATE │
 * ├──────────────┼──────────┼────────────┼──────────┼────────────┤
 * │  ACCT_NO     │CUST_ID   │  FROM_BANK │  TO_BANK │  CHANNEL   │
 * └──────────────┴──────────┴────────────┴──────────┴────────────┘
 * Total: 9 columns (no SOURCE_SYS column — this adapter always sets CBS)
 *
 * Implements TransactionAdapter  —  IS-A TransactionAdapter
 */
public class CbsAdapter implements TransactionAdapter {

    private static final String DELIMITER       = "\\|";
    private static final int    EXPECTED_COLS   = 9;
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----------------------------------------------------------------
    // TransactionAdapter contract
    // ----------------------------------------------------------------

    /**
     * Adapts a single pipe-delimited CBS line into IncomingTransaction.
     *
     * Example line:
     *   CBS-2024-0301|CREDIT|500000.00|INR|2024-03-01|ACC-1001|CUST-101|SBI|HDFC|RTGS
     */
    @Override
    public IncomingTransaction adapt(String rawPayload) {
        if (rawPayload == null || rawPayload.isBlank()) {
            throw new IllegalArgumentException("CbsAdapter: rawPayload is empty.");
        }

        String[] col = rawPayload.split(DELIMITER, -1);
        if (col.length != EXPECTED_COLS) {
            throw new IllegalArgumentException(
                "CbsAdapter: expected " + EXPECTED_COLS + " columns, got "
                + col.length + " → " + rawPayload);
        }

        IncomingTransaction txn = new IncomingTransaction();

        // Map each column to the corresponding field
        txn.setSourceRef    (trim(col[0]));
        txn.setTxnType      (TransactionType.valueOf(trim(col[1]).toUpperCase()));
        txn.setAmount       (new BigDecimal(trim(col[2])));
        txn.setCurrency     (trim(col[3]).toUpperCase());
        txn.setValueDate    (LocalDate.parse(trim(col[4]), DATE_FMT));
        txn.setAccountNumber(trim(col[5]));
        txn.setCustomerId   (trim(col[6]));
        txn.setFromBank     (trim(col[7]));
        txn.setToBank       (trim(col[8]));

        // CBS always comes from RTGS channel (high-value core banking)
        txn.setChannel      (ChannelType.RTGS);

        // Fixed for this adapter
        txn.setSourceSystem (SourceType.CBS);
        txn.setStatus       (TransactionStatus.RECEIVED);
        txn.setRawPayload   (rawPayload);
        txn.setNormalizedPayload(buildNormalized(txn));

        return txn;
    }

    /** Reads the entire .txt file; skips the header row. */
    @Override
    public List<IncomingTransaction> adaptFile(String filePath) {
        List<IncomingTransaction> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (isHeader) { isHeader = false; continue; }   // skip header
                try {
                    list.add(adapt(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("[CbsAdapter] Skipping bad line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("CbsAdapter: cannot read file: " + filePath, e);
        }

        System.out.println("[CbsAdapter] Parsed " + list.size() + " transactions from " + filePath);
        return list;
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.CBS;
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private String trim(String s) { return s == null ? "" : s.trim(); }

    private String buildNormalized(IncomingTransaction t) {
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
             + "sourceSystem="  + t.getSourceSystem();
    }
}