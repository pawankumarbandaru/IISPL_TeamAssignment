package com.iispl.entity;

public final class Transaction {

<<<<<<< HEAD
}
=======
public abstract class Transaction {

    private Long txnId;
    private Long debitAccountId;
    private Long creditAccountId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime txnDate;
    private LocalDate valueDate;
    private TransactionStatus status;
    private String referenceNumber;
    private BankType fromBank;
    private BankType toBank;
    

    public BankType getFromBank() {
		return fromBank;
	}
	public void setFromBank(BankType fromBank) {
		this.fromBank = fromBank;
	}
	public BankType getToBank() {
		return toBank;
	}
	public void setToBank(BankType toBank) {
		this.toBank = toBank;
	}
	// Getters & Setters
    public Long getTxnId() { return txnId; }
    public void setTxnId(Long txnId) { this.txnId = txnId; }

    public Long getDebitAccountId() { return debitAccountId; }
    public void setDebitAccountId(Long debitAccountId) { this.debitAccountId = debitAccountId; }

    public Long getCreditAccountId() { return creditAccountId; }
    public void setCreditAccountId(Long creditAccountId) { this.creditAccountId = creditAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDateTime getTxnDate() { return txnDate; }
    public void setTxnDate(LocalDateTime txnDate) { this.txnDate = txnDate; }

    public LocalDate getValueDate() { return valueDate; }
    public void setValueDate(LocalDate valueDate) { this.valueDate = valueDate; }

    public TransactionStatus getStatus() { return status; }
    
    public void setStatus(TransactionStatus status) { this.status = status; }
    public enum TransactionStatus {
        VALIDATED, PENDING, FAILED
    }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
}
>>>>>>> 6b3b002 (Intial)
