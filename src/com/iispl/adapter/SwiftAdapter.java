package com.iispl.adapter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iispl.entity.IncomingTransaction;
import com.iispl.enums.ChannelType;
import com.iispl.enums.SourceType;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

/**
 * ════════════════════════════════════════════════════════════════════
 *  SwiftAdapter  —  SWIFT Cross-Border Transfer Adapter
 * ════════════════════════════════════════════════════════════════════
 *
 * SWIFT (Society for Worldwide Interbank Financial Telecommunication)
 * is the global standard for cross-border payments. Modern SWIFT APIs
 * send data as JSON (gpi / ISO 20022 inspired format).
 *
 * Key SWIFT characteristics reflected in the format:
 *   • fromBank and toBank use BIC (Bank Identifier Code), e.g. "DEUTDEDB"
 *   • Currency is typically foreign (USD, EUR, GBP, JPY …)
 *   • A correspondentBank field carries the intermediary BIC
 *   • swiftRef is the UETR (Unique End-to-end Transaction Reference)
 *
 * File format  : .json  (array of flat JSON objects)
 * Source system: SWIFT
 * Channel used : SWIFT (always)
 *
 * JSON fields per record:
 *   sourceRef, txnType, amount, currency, valueDate,
 *   accountNumber, customerId, fromBic, toBic, correspondentBic
 *
 * No external JSON library used — parsed manually using collections.
 *
 * Implements TransactionAdapter  —  IS-A TransactionAdapter
 */
public class SwiftAdapter implements TransactionAdapter {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----------------------------------------------------------------
    // TransactionAdapter contract
    // ----------------------------------------------------------------

    /**
     * Adapts a single JSON object string into IncomingTransaction.
     *
     * Example rawPayload:
     * {
     *   "sourceRef": "SWIFT-20240301-001", "txnType": "CREDIT",
     *   "amount": "150000.00", "currency": "USD", "valueDate": "2024-03-01",
     *   "accountNumber": "ACC-9001", "customerId": "CUST-301",
     *   "fromBic": "DEUTDEDB", "toBic": "HDFCINBB",
     *   "correspondentBic": "CITIUS33"
     * }
     */
    @Override
    public IncomingTransaction adapt(String rawPayload) {
        if (rawPayload == null || rawPayload.isBlank()) {
            throw new IllegalArgumentException("SwiftAdapter: rawPayload is empty.");
        }

        Map<String, String> fields = parseJsonObject(rawPayload);

        IncomingTransaction txn = new IncomingTransaction();

        txn.setSourceRef    (require(fields, "sourceRef",     rawPayload));
        txn.setTxnType      (TransactionType.valueOf(require(fields, "txnType", rawPayload).toUpperCase()));
        txn.setAmount       (new BigDecimal(require(fields, "amount", rawPayload)));
        txn.setCurrency     (require(fields, "currency",      rawPayload).toUpperCase());
        txn.setValueDate    (LocalDate.parse(require(fields, "valueDate", rawPayload), DATE_FMT));
        txn.setAccountNumber(require(fields, "accountNumber", rawPayload));
        txn.setCustomerId   (require(fields, "customerId",    rawPayload));
        txn.setFromBank     (require(fields, "fromBic",       rawPayload));  // BIC code
        txn.setToBank       (require(fields, "toBic",         rawPayload));  // BIC code

        txn.setChannel      (ChannelType.SWIFT);
        txn.setSourceSystem (SourceType.SWIFT);
        txn.setStatus       (TransactionStatus.RECEIVED);
        txn.setRawPayload   (rawPayload);

        // correspondentBic is optional — include if present
        String correspondentBic = fields.getOrDefault("correspondentBic", "");
        txn.setNormalizedPayload(buildNormalized(txn, correspondentBic));

        return txn;
    }

    /**
     * Reads a .json file containing an array of SWIFT transaction objects
     * and adapts each one.
     *
     * Expected file structure:  [ { ... }, { ... }, ... ]
     */
    @Override
    public List<IncomingTransaction> adaptFile(String filePath) {
        List<IncomingTransaction> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("SwiftAdapter: cannot read file: " + filePath, e);
        }

        for (String obj : extractJsonObjects(sb.toString())) {
            try {
                list.add(adapt(obj));
            } catch (IllegalArgumentException e) {
                System.err.println("[SwiftAdapter] Skipping bad object: " + e.getMessage());
            }
        }

        System.out.println("[SwiftAdapter] Parsed " + list.size() + " transactions from " + filePath);
        return list;
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.SWIFT;
    }

    // ----------------------------------------------------------------
    // Private — manual JSON parsing (no external libraries)
    // ----------------------------------------------------------------

    /**
     * Extracts every { ... } block from a JSON array string.
     * Works for flat objects (no nested objects inside field values).
     */
    private List<String> extractJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    objects.add(json.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return objects;
    }

    /**
     * Parses a flat JSON object into a Map<String, String>.
     * Handles both quoted string values and unquoted numeric values.
     */
    private Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        // Strip outer braces
        String content = json.trim();
        if (content.startsWith("{")) content = content.substring(1);
        if (content.endsWith("}"))  content = content.substring(0, content.length() - 1);

        // Split on commas that are not inside quotes
        String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isBlank()) continue;
            int colon = pair.indexOf(':');
            if (colon < 0) continue;
            String key   = pair.substring(0, colon).trim().replace("\"", "");
            String value = pair.substring(colon + 1).trim().replace("\"", "");
            map.put(key, value);
        }
        return map;
    }

    private String require(Map<String, String> fields, String key, String raw) {
        String v = fields.get(key);
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(
                "SwiftAdapter: missing field '" + key + "' in: " + raw);
        }
        return v;
    }

    private String buildNormalized(IncomingTransaction t, String correspondentBic) {
        return "sourceRef="        + t.getSourceRef()     + "|"
             + "txnType="          + t.getTxnType()        + "|"
             + "amount="           + t.getAmount()          + "|"
             + "currency="         + t.getCurrency()        + "|"
             + "valueDate="        + t.getValueDate()       + "|"
             + "accountNumber="    + t.getAccountNumber()   + "|"
             + "customerId="       + t.getCustomerId()      + "|"
             + "fromBic="          + t.getFromBank()        + "|"
             + "toBic="            + t.getToBank()          + "|"
             + "correspondentBic=" + correspondentBic       + "|"
             + "channel="          + t.getChannel()         + "|"
             + "sourceSystem="     + t.getSourceSystem();
    }
}
