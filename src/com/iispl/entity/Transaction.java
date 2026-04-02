package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;

public abstract class Transaction {

    protected final Long txnId;
    protected final Long debitAccountId;
    protected final Long creditAccountId;
    protected final BigDecimal amount;
    protected final String currency;
    protected final LocalDateTime txnDate;
    protected final LocalDate valueDate;
    protected final TransactionStatus status;
    protected final String referenceNumber;

    protected Transaction(Long txnId, Long debitAccountId, Long creditAccountId,
                          BigDecimal amount, String currency,
                          LocalDateTime txnDate, LocalDate valueDate,
                          TransactionStatus status, String referenceNumber) {

        this.txnId = txnId;
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.amount = amount;
        this.currency = currency;
        this.txnDate = txnDate;
        this.valueDate = valueDate;
        this.status = status;
        this.referenceNumber = referenceNumber;
    }

    public Long getTxnId() {
		return txnId;
	}

	public Long getDebitAccountId() {
		return debitAccountId;
	}

	public Long getCreditAccountId() {
		return creditAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public LocalDateTime getTxnDate() {
		return txnDate;
	}

	public LocalDate getValueDate() {
		return valueDate;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public String getReferenceNumber() { return referenceNumber; }
    
    @Override
    public String toString() {
        return "Transaction {" +
                "\n  txnId=" + txnId +
                ",\n  debitAccountId=" + debitAccountId +
                ",\n  creditAccountId=" + creditAccountId +
                ",\n  amount=" + amount +
                ",\n  currency='" + currency + '\'' +
                ",\n  txnDate=" + txnDate +
                ",\n  valueDate=" + valueDate +
                ",\n  status=" + status +
                ",\n  referenceNumber='" + referenceNumber + '\'' +
                "\n}";
    }
}