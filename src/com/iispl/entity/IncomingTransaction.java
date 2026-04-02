package com.iispl.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.ChannelType;
import com.iispl.enums.SourceType;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

/**
 * ════════════════════════════════════════════════════════════════════
 *  IncomingTransaction  —  Canonical POJO
 * ════════════════════════════════════════════════════════════════════
 *
 * Every transaction — regardless of whether it came from CBS, RTGS,
 * SWIFT, NEFT/UPI, or a Fintech API — is normalised into this single
 * class by the corresponding adapter.
 *
 * No inheritance from BaseEntity here (kept simple as requested).
 *
 * Fields from the architecture document:
 *   sourceSystem, sourceRef, txnType, amount, currency, valueDate
 *
 * Extra fields added per assignment requirement:
 *   accountNumber, customerId, fromBank, toBank, channel
 *
 * ProcessingStatus replaced by TransactionStatus (also per requirement).
 */
public class IncomingTransaction {

    // ---------------------------------------------------------------
    // Core fields (from architecture doc)
    // ---------------------------------------------------------------

    /** Auto-assigned sequential ID (set by AdapterRegistry). */
    private Long incomingTxnId;

    /** Which source system sent this transaction. */
    private SourceType sourceSystem;

    /** The unique reference ID from the source system. */
    private String sourceRef;

    /** The raw string payload as received — kept for audit / replay. */
    private String rawPayload;

    /** Nature of the transaction: CREDIT, DEBIT, REVERSAL … */
    private TransactionType txnType;

    /** Transaction amount. BigDecimal ensures no floating-point errors. */
    private BigDecimal amount;

    /** ISO 4217 currency code, e.g. "INR", "USD", "EUR". */
    private String currency;

    /** Date on which funds should be available or debited. */
    private LocalDate valueDate;

    /**
     * Lifecycle status — replaces ProcessingStatus as per requirement.
     * Default: RECEIVED (set in constructor).
     */
    private TransactionStatus status;

    /** Exact timestamp when this record was read from the source. */
    private LocalDateTime ingestTimestamp;

    /**
     * Standardised "key=value|key=value" string built by each adapter.
     * Downstream processors use this so they never re-parse the original format.
     */
    private String normalizedPayload;

    // ---------------------------------------------------------------
    // Extra fields (added per assignment requirement)
    // ---------------------------------------------------------------

    /**
     * The primary account number involved in this transaction.
     * For a DEBIT this is the source account;
     * for a CREDIT this is the destination account.
     */
    private String accountNumber;

    /**
     * ID of the customer who owns the accountNumber above.
     */
    private String customerId;

    /**
     * Originating / sending bank.
     * Can be a bank name ("SBI"), IFSC code ("SBIN0001234"),
     * or SWIFT BIC ("DEUTDEDB") depending on the source system.
     */
    private String fromBank;

    /**
     * Beneficiary / receiving bank.
     * Same format as fromBank.
     */
    private String toBank;

    /**
     * Payment rail through which this transaction will be routed
     * (RTGS for high-value, NEFT for batch, UPI for instant retail, etc.).
     */
    private ChannelType channel;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public IncomingTransaction() {
        this.status          = TransactionStatus.RECEIVED;  // always starts here
        this.ingestTimestamp = LocalDateTime.now();
    }

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public Long getIncomingTxnId() { return incomingTxnId; }
    public void setIncomingTxnId(Long incomingTxnId) { this.incomingTxnId = incomingTxnId; }

    public SourceType getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(SourceType sourceSystem) { this.sourceSystem = sourceSystem; }

    public String getSourceRef() { return sourceRef; }
    public void setSourceRef(String sourceRef) { this.sourceRef = sourceRef; }

    public String getRawPayload() { return rawPayload; }
    public void setRawPayload(String rawPayload) { this.rawPayload = rawPayload; }

    public TransactionType getTxnType() { return txnType; }
    public void setTxnType(TransactionType txnType) { this.txnType = txnType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getValueDate() { return valueDate; }
    public void setValueDate(LocalDate valueDate) { this.valueDate = valueDate; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public LocalDateTime getIngestTimestamp() { return ingestTimestamp; }
    public void setIngestTimestamp(LocalDateTime ingestTimestamp) { this.ingestTimestamp = ingestTimestamp; }

    public String getNormalizedPayload() { return normalizedPayload; }
    public void setNormalizedPayload(String normalizedPayload) { this.normalizedPayload = normalizedPayload; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getFromBank() { return fromBank; }
    public void setFromBank(String fromBank) { this.fromBank = fromBank; }

    public String getToBank() { return toBank; }
    public void setToBank(String toBank) { this.toBank = toBank; }

    public ChannelType getChannel() { return channel; }
    public void setChannel(ChannelType channel) { this.channel = channel; }

    // ---------------------------------------------------------------
    // toString  —  prints every field for easy debugging
    // ---------------------------------------------------------------

    @Override
    public String toString() {
        return "IncomingTransaction {" +
               "\n  incomingTxnId   = " + incomingTxnId +
               "\n  sourceSystem    = " + sourceSystem +
               "\n  sourceRef       = " + sourceRef +
               "\n  txnType         = " + txnType +
               "\n  amount          = " + amount + " " + currency +
               "\n  accountNumber   = " + accountNumber +
               "\n  customerId      = " + customerId +
               "\n  fromBank        = " + fromBank +
               "\n  toBank          = " + toBank +
               "\n  channel         = " + channel +
               "\n  valueDate       = " + valueDate +
               "\n  status          = " + status +
               "\n  ingestTimestamp = " + ingestTimestamp +
               "\n  normalizedPayload = " + normalizedPayload +
               "\n}";
    }
}
