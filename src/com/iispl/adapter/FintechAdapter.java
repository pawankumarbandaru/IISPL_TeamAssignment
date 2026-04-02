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
 *  FintechAdapter  —  Third-Party Fintech API Adapter
 * ════════════════════════════════════════════════════════════════════
 *
 * Fintech partners (payment gateways, wallets, BNPL providers, etc.)
 * push transactions via REST webhooks as JSON. The payload is richer
 * than bank-to-bank formats — it includes:
 *
 *   • partnerTxnId   — the Fintech's own transaction reference
 *   • partnerName    — name of the Fintech partner (e.g., "RAZORPAY")
 *   • paymentMode    — mode used: WALLET, CARD, UPI, NETBANKING, EMI
 *   • feeAmount      — platform fee charged by the Fintech
 *   • netAmount      — amount after deducting feeAmount
 *
 * The partnerName is stored in fromBank (originating party),
 * and the merchant's bank IFSC goes into toBank.
 *
 * File format  : .json  (array of flat JSON objects from webhook batches)
 * Source system: FINTECH
 * Channel used : Derived from paymentMode field in each record
 *                (UPI → ChannelType.UPI, else → ChannelType.ACH)
 *
 * JSON fields per record:
 *   sourceRef, partnerTxnId, partnerName, txnType,
 *   amount, feeAmount, currency, valueDate,
 *   accountNumber, customerId, toBankIfsc, paymentMode
 *
 * Implements TransactionAdapter  —  IS-A TransactionAdapter
 */
public class FintechAdapter implements TransactionAdapter {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----------------------------------------------------------------
    // TransactionAdapter contract
    // ----------------------------------------------------------------

    /**
     * Adapts a single Fintech JSON object into IncomingTransaction.
     *
     * Example rawPayload:
     * {
     *   "sourceRef"    : "FIN-2024-0301-001",
     *   "partnerTxnId" : "RZP-PAY-123456",
     *   "partnerName"  : "RAZORPAY",
     *   "txnType"      : "CREDIT",
     *   "amount"       : "4999.00",
     *   "feeAmount"    : "49.99",
     *   "currency"     : "INR",
     *   "valueDate"    : "2024-03-01",
     *   "accountNumber": "ACC-5001",
     *   "customerId"   : "CUST-501",
     *   "toBankIfsc"   : "ICIC0001234",
     *   "paymentMode"  : "UPI"
     * }
     */
    @Override
    public IncomingTransaction adapt(String rawPayload) {
        if (rawPayload == null || rawPayload.isBlank()) {
            throw new IllegalArgumentException("FintechAdapter: rawPayload is empty.");
        }

        Map<String, String> f = parseJsonObject(rawPayload);

        // Net amount = amount - feeAmount (the actual amount settled)
        BigDecimal amount    = new BigDecimal(require(f, "amount",    rawPayload));
        BigDecimal feeAmount = new BigDecimal(f.getOrDefault("feeAmount", "0.00"));
        BigDecimal netAmount = amount.subtract(feeAmount);

        // Channel derived from paymentMode
        String paymentMode = require(f, "paymentMode", rawPayload).toUpperCase();
        ChannelType channel = paymentMode.equals("UPI")
                            ? ChannelType.UPI
                            : ChannelType.ACH;

        String partnerName = require(f, "partnerName", rawPayload);

        IncomingTransaction txn = new IncomingTransaction();

        // sourceRef from Fintech combines our ref and their ref
        txn.setSourceRef    (require(f, "sourceRef", rawPayload)
                             + " [" + f.getOrDefault("partnerTxnId", "") + "]");
        txn.setTxnType      (TransactionType.valueOf(require(f, "txnType", rawPayload).toUpperCase()));
        txn.setAmount       (netAmount);            // settle the net amount
        txn.setCurrency     (require(f, "currency", rawPayload).toUpperCase());
        txn.setValueDate    (LocalDate.parse(require(f, "valueDate", rawPayload), DATE_FMT));
        txn.setAccountNumber(require(f, "accountNumber", rawPayload));
        txn.setCustomerId   (require(f, "customerId",    rawPayload));
        txn.setFromBank     (partnerName);           // Fintech partner is the sender
        txn.setToBank       (require(f, "toBankIfsc",    rawPayload));
        txn.setChannel      (channel);
        txn.setSourceSystem (SourceType.FINTECH);

        txn.setStatus      (TransactionStatus.RECEIVED);
        txn.setRawPayload  (rawPayload);
        txn.setNormalizedPayload(
            buildNormalized(txn, partnerName, amount, feeAmount, paymentMode));

        return txn;
    }

    /**
     * Reads a .json file of Fintech webhook records (JSON array)
     * and adapts each object.
     */
    @Override
    public List<IncomingTransaction> adaptFile(String filePath) {
        List<IncomingTransaction> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("FintechAdapter: cannot read file: " + filePath, e);
        }

        for (String obj : extractJsonObjects(sb.toString())) {
            try {
                list.add(adapt(obj));
            } catch (IllegalArgumentException e) {
                System.err.println("[FintechAdapter] Skipping bad object: " + e.getMessage());
            }
        }

        System.out.println("[FintechAdapter] Parsed " + list.size() + " transactions from " + filePath);
        return list;
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.FINTECH;
    }

    // ----------------------------------------------------------------
    // Private — manual JSON parsing
    // ----------------------------------------------------------------

    private List<String> extractJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int depth = 0, start = -1;
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

    private Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        String content = json.trim();
        if (content.startsWith("{")) content = content.substring(1);
        if (content.endsWith("}"))  content = content.substring(0, content.length() - 1);

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
                "FintechAdapter: missing field '" + key + "' in: " + raw);
        }
        return v;
    }

    private String buildNormalized(IncomingTransaction t,
                                   String partnerName,
                                   BigDecimal grossAmount,
                                   BigDecimal feeAmount,
                                   String paymentMode) {
        return "sourceRef="     + t.getSourceRef()     + "|"
             + "txnType="       + t.getTxnType()        + "|"
             + "grossAmount="   + grossAmount            + "|"
             + "feeAmount="     + feeAmount              + "|"
             + "netAmount="     + t.getAmount()          + "|"
             + "currency="      + t.getCurrency()        + "|"
             + "valueDate="     + t.getValueDate()       + "|"
             + "accountNumber=" + t.getAccountNumber()   + "|"
             + "customerId="    + t.getCustomerId()      + "|"
             + "partnerName="   + partnerName            + "|"
             + "toBank="        + t.getToBank()          + "|"
             + "paymentMode="   + paymentMode            + "|"
             + "channel="       + t.getChannel()         + "|"
             + "sourceSystem="  + t.getSourceSystem();
    }
}
