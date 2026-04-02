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
 *  NeftUpiAdapter  —  NEFT / UPI Adapter
 * ════════════════════════════════════════════════════════════════════
 *
 * A single adapter handles both NEFT and UPI because they share the
 * same file format — only the CHANNEL column differs. The SourceType
 * is derived from that CHANNEL column at runtime.
 *
 * NEFT  : National Electronic Funds Transfer — batch settlement,
 *         processed in hourly windows by RBI, no minimum amount.
 * UPI   : Unified Payments Interface — instant 24×7 retail payments
 *         via VPA (Virtual Payment Address), limit ₹1 lakh/txn.
 *
 * Key characteristics reflected in the format:
 *   • CHANNEL column can be "NEFT" or "UPI"
 *   • fromBank / toBank stored as IFSC codes for NEFT,
 *     VPA handles (@okaxis, @paytm) for UPI
 *   • UPI transactions have a UPI_REF field in the source reference
 *
 * File format  : .csv  (comma-delimited, first row is header)
 * Channel used : NEFT or UPI (read from each record)
 *
 * Column order:
 * SOURCE_REF , TXN_TYPE , AMOUNT , CURRENCY , VALUE_DATE ,
 * ACCT_NO    , CUST_ID  , FROM_BANK , TO_BANK , CHANNEL
 *
 * Total: 10 columns
 *
 * Implements TransactionAdapter  —  IS-A TransactionAdapter
 */
public class NeftUpiAdapter implements TransactionAdapter {

    private static final String DELIMITER     = ",";
    private static final int    EXPECTED_COLS = 10;
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----------------------------------------------------------------
    // TransactionAdapter contract
    // ----------------------------------------------------------------

    /**
     * Adapts a single CSV line into IncomingTransaction.
     * The CHANNEL column (index 9) decides whether the record is
     * NEFT or UPI — and sets SourceType accordingly.
     *
     * Example NEFT line:
     *   NEFT-20240301-001,CREDIT,10000.00,INR,2024-03-01,ACC-2001,CUST-201,SBIN0001234,HDFC0000123,NEFT
     *
     * Example UPI line:
     *   UPI-20240301-001,CREDIT,1500.00,INR,2024-03-01,ACC-2003,CUST-203,user@okaxis,merchant@paytm,UPI
     */
    @Override
    public IncomingTransaction adapt(String rawPayload) {
        if (rawPayload == null || rawPayload.isBlank()) {
            throw new IllegalArgumentException("NeftUpiAdapter: rawPayload is empty.");
        }

        String[] col = splitCsv(rawPayload);
        if (col.length != EXPECTED_COLS) {
            throw new IllegalArgumentException(
                "NeftUpiAdapter: expected " + EXPECTED_COLS + " columns, got "
                + col.length + " → " + rawPayload);
        }

        // Determine channel first — drives SourceType
        String channelStr = trim(col[9]).toUpperCase();
        ChannelType channel;
        SourceType  sourceType;

        if (channelStr.equals("NEFT")) {
            channel    = ChannelType.NEFT;
            sourceType = SourceType.NEFT;
        } else if (channelStr.equals("UPI")) {
            channel    = ChannelType.UPI;
            sourceType = SourceType.UPI;
        } else {
            throw new IllegalArgumentException(
                "NeftUpiAdapter: unknown channel '" + channelStr +
                "' — expected NEFT or UPI.");
        }

        IncomingTransaction txn = new IncomingTransaction();

        txn.setSourceRef    (trim(col[0]));
        txn.setTxnType      (TransactionType.valueOf(trim(col[1]).toUpperCase()));
        txn.setAmount       (new BigDecimal(trim(col[2])));
        txn.setCurrency     (trim(col[3]).toUpperCase());
        txn.setValueDate    (LocalDate.parse(trim(col[4]), DATE_FMT));
        txn.setAccountNumber(trim(col[5]));
        txn.setCustomerId   (trim(col[6]));
        txn.setFromBank     (trim(col[7]));
        txn.setToBank       (trim(col[8]));
        txn.setChannel      (channel);
        txn.setSourceSystem (sourceType);

        txn.setStatus      (TransactionStatus.RECEIVED);
        txn.setRawPayload  (rawPayload);
        txn.setNormalizedPayload(buildNormalized(txn));

        return txn;
    }

    /** Reads the CSV file; skips the header row. */
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
                    System.err.println("[NeftUpiAdapter] Skipping bad row: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("NeftUpiAdapter: cannot read file: " + filePath, e);
        }

        System.out.println("[NeftUpiAdapter] Parsed " + list.size() + " transactions from " + filePath);
        return list;
    }

    /**
     * Returns NEFT as the primary SourceType for registry lookup.
     * UPI records are also handled by this same adapter instance.
     */
    @Override
    public SourceType getSourceType() {
        return SourceType.NEFT;
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    /**
     * Splits a CSV line respecting fields quoted with double-quotes.
     * A comma inside quotes is NOT treated as a delimiter.
     */
    private String[] splitCsv(String line) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }

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