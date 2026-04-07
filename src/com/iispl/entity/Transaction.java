package com.iispl.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {

    public enum TxnType   { CREDIT, DEBIT, REVERSAL }
    public enum TxnStatus { PENDING, PROCESSED, FAILED }
    public enum Channel   { NEFT, RTGS, IMPS, UPI, SWIFT }

    private final String        transactionId;
    private final TxnType       transactionType;
    private final double        amount;
    private final String        fromBank;
    private final String        toBank;
    private final String        accountId;
    private final Channel       channel;
    private final LocalDateTime transactionDate;
    private final LocalDate     valueDate;
    private TxnStatus           status;

    

    public Transaction(String transactionId, TxnType transactionType, double amount, String fromBank, String toBank,
			String accountId, Channel channel, LocalDateTime transactionDate, LocalDate valueDate, TxnStatus status) {
		
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.fromBank = fromBank;
		this.toBank = toBank;
		this.accountId = accountId;
		this.channel = channel;
		this.transactionDate = transactionDate;
		this.valueDate = valueDate;
		this.status = status.PENDING;
	}


	/**
     * Batch grouping key.
     * fromBank|toBank|valueDate — direction always matters.
     * SBI->HDFC and HDFC->SBI are always different batches.
     */
    public String getBatchKey() {
        return fromBank + "|" + toBank + "|" + valueDate;
    }

    public String        getTxnId()     { return txnId; }
    public TxnType       getTxnType()   { return txnType; }
    public double        getAmount()    { return amount; }
    public String        getFromBank()  { return fromBank; }
    public String        getToBank()    { return toBank; }
    public String        getAccountId() { return accountId; }
    public Channel       getChannel()   { return channel; }
    public LocalDateTime getTxnDate()   { return txnDate; }
    public LocalDate     getValueDate() { return valueDate; }
    public TxnStatus     getStatus()    { return status; }
    public void          setStatus(TxnStatus s) { this.status = s; }

    @Override
    public String toString() {
        return String.format("[Txn %-12s %-9s %10.2f  %-14s -> %-14s %-5s %s]",
                txnId, txnType, amount, fromBank, toBank, channel, valueDate);
    }
}