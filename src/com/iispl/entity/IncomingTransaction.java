package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class IncomingTransaction {

    private Long incomingTxnId;
    private String sourceSystem;
    private String sourceRef;
    private TransactionType txnType;
    private BigDecimal transactionAmount;
    protected String accountNumber;
    protected Long customerId;
    protected String fromBank;
    protected String toBank;
    protected String channel;
    private LocalDate valueDate;
    private TransactionStatus status;
    private LocalDateTime ingestTimestamp;
    private String normalizedPayload;

    //  Default constructor
    public IncomingTransaction() {}

    // constructor
    public IncomingTransaction(Long incomingTxnId, String sourceSystem, String sourceRef,
                               TransactionType txnType, BigDecimal transactionAmount,
                               String accountNumber, Long customerId,
                               String fromBank, String toBank, String channel,
                               LocalDate valueDate, TransactionStatus status,
                               LocalDateTime ingestTimestamp, String normalizedPayload) {

        this.incomingTxnId = incomingTxnId;
        this.sourceSystem = sourceSystem;
        this.sourceRef = sourceRef;
        this.txnType = txnType;
        this.transactionAmount = transactionAmount;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.fromBank = fromBank;
        this.toBank = toBank;
        this.channel = channel;
        this.valueDate = valueDate;
        this.status = status;
        this.ingestTimestamp = ingestTimestamp;
        this.normalizedPayload = normalizedPayload;
    }

    
    public Long getIncomingTxnId() {
		return incomingTxnId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public TransactionType getTxnType() {
		return txnType;
	}

	public BigDecimal getAmount() {
		return transactionAmount;
	}
	public String getAccountNumber() {
		return accountNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public String getFromBank() {
		return fromBank;
	}

	public String getToBank() {
		return toBank;
	}

	public String getChannel() {
		return channel;
	}
	
	public LocalDate getValueDate() {
		return valueDate;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public LocalDateTime getIngestTimestamp() {
		return ingestTimestamp;
	}

	public String getNormalizedPayload() {
		return normalizedPayload;
	}

	public String getSourceRef() {
        return sourceRef;
    }

    @Override
    public String toString() {
        return "IncomingTransaction {" +
                "\n  incomingTxnId=" + incomingTxnId +
                ",\n  sourceSystem='" + sourceSystem + '\'' +
                ",\n  sourceRef='" + sourceRef + '\'' +
                ",\n  txnType=" + txnType +
                ",\n  amount=" + amount +
                ",\n  accountNumber='" + accountNumber + '\'' +
                ",\n  customerId=" + customerId +
                ",\n  fromBank='" + fromBank + '\'' +
                ",\n  toBank='" + toBank + '\'' +
                ",\n  channel='" + channel + '\'' +
                ",\n  valueDate=" + valueDate +
                ",\n  status=" + status +
                ",\n  ingestTimestamp=" + ingestTimestamp +
                ",\n  normalizedPayload='" + normalizedPayload + '\'' +
                "\n}";
    }
}