package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class IncomingTransaction {

    private UUID incomingTxnId;
    private String sourceSystem;
    private String sourceRef;
    private String rawPayload;
    private TransactionType txnType;
    private BigDecimal transactionAmount;
    protected String accountNumber;
    protected Long customerId;
    protected String fromBank;
    protected String toBank;
    protected String channel;
    private LocalDateTime valueDateTime;
    private TransactionStatus status;
    private LocalDateTime txnDateTime;
    private String normalizedPayload;
    // Only for InterBank
    private String correspondent;
    
    // Only for Reversal
    private String originalRef;

    // constructor
    public IncomingTransaction(UUID incomingTxnId, String sourceSystem, String sourceRef,
                               String rawPayload,TransactionType txnType, 
                               BigDecimal transactionAmount,
                               String accountNumber, Long customerId,
                               String fromBank, String toBank, String channel,
                               LocalDateTime valueDateTime, TransactionStatus status,
                               LocalDateTime txnDateTime, String normalizedPayload) {

        this.incomingTxnId = incomingTxnId;
        this.sourceSystem = sourceSystem;
        this.sourceRef = sourceRef;
        this.rawPayload = rawPayload;
        this.txnType = txnType;
        this.transactionAmount = transactionAmount;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.fromBank = fromBank;
        this.toBank = toBank;
        this.channel = channel;
        this.valueDateTime = valueDateTime;
        this.status = status;
        this.txnDateTime = txnDateTime;
        this.normalizedPayload = normalizedPayload;
    }
    
    public UUID getIncomingTxnId() {
		return incomingTxnId;
	}

	public void setIncomingTxnId(UUID incomingTxnId) {
		this.incomingTxnId = incomingTxnId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getRawPayload() {
		return rawPayload;
	}

	public void setRawPayload(String rawPayload) {
		this.rawPayload = rawPayload;
	}

	public TransactionType getTxnType() {
		return txnType;
	}

	public void setTxnType(TransactionType txnType) {
		this.txnType = txnType;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFromBank() {
		return fromBank;
	}

	public void setFromBank(String fromBank) {
		this.fromBank = fromBank;
	}

	public String getToBank() {
		return toBank;
	}

	public void setToBank(String toBank) {
		this.toBank = toBank;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public LocalDateTime getValueDateTime() {
		return valueDateTime;
	}

	public void setValueDateTime(LocalDateTime valueDateTime) {
		this.valueDateTime = valueDateTime;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public LocalDateTime getTxnDateTime() {
		return txnDateTime;
	}

	public void setTxnDateTime(LocalDateTime txnDateTime) {
		this.txnDateTime = txnDateTime;
	}

	public String getNormalizedPayload() {
		return normalizedPayload;
	}

	public void setNormalizedPayload(String normalizedPayload) {
		this.normalizedPayload = normalizedPayload;
	}

	public String getCorrespondent() {
		return correspondent;
	}

	public void setCorrespondent(String correspondent) {
		this.correspondent = correspondent;
	}

	public String getOriginalRef() {
		return originalRef;
	}

	public void setOriginalRef(String originalRef) {
		this.originalRef = originalRef;
	}

}