package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.iispl.enums.BankName;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public abstract class Transaction {
	private UUID txnId;
	private BigDecimal amount;
	private TransactionType txnType;
	private TransactionStatus txnStatus;
	private BankName fromBank;
	private BankName toBank;
	private String channel;
	private LocalDateTime txnDateTime;
	private LocalDateTime valueDateTime;
	
	// Empty Constructor
	public Transaction() {	}
	
	public Transaction(UUID txnId, BigDecimal amount, TransactionType txnType, TransactionStatus txnStatus,
			BankName fromBank, BankName toBank, String channel, LocalDateTime txnDateTime,
			LocalDateTime valueDateTime) {
		this.txnId = txnId;
		this.amount = amount;
		this.txnType = txnType;
		this.txnStatus = txnStatus;
		this.fromBank = fromBank;
		this.toBank = toBank;
		this.channel = channel;
		this.txnDateTime = txnDateTime;
		this.valueDateTime = valueDateTime;
	}
	
	
	public UUID getTxnId() {
		return txnId;
	}
	public void setTxnId(UUID txnId) {
		this.txnId = txnId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public TransactionType getTxnType() {
		return txnType;
	}
	public void setTxnType(TransactionType txnType) {
		this.txnType = txnType;
	}
	public TransactionStatus getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(TransactionStatus txnStatus) {
		this.txnStatus = txnStatus;
	}
	public BankName getFromBank() {
		return fromBank;
	}
	public void setFromBank(BankName fromBank) {
		this.fromBank = fromBank;
	}
	public BankName getToBank() {
		return toBank;
	}
	public void setToBank(BankName toBank) {
		this.toBank = toBank;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public LocalDateTime getTxnDateTime() {
		return txnDateTime;
	}
	public void setTxnDateTime(LocalDateTime txnDateTime) {
		this.txnDateTime = txnDateTime;
	}
	public LocalDateTime getValueDateTime() {
		return valueDateTime;
	}
	public void setValueDateTime(LocalDateTime valueDateTime) {
		this.valueDateTime = valueDateTime;
	}
	
	
}
