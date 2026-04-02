package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.TransactionType;

public class IncomingTransaction {

    private String sourceRef;
    private TransactionType txnType;
    private BigDecimal amount;
    private String currency;
    private LocalDate valueDate;

    private Long debitAccountId;
    private Long creditAccountId;

    // getters & setters

    public String getSourceRef() { return sourceRef; }
    public void setSourceRef(String sourceRef) { this.sourceRef = sourceRef; }

    public TransactionType getTxnType() { return txnType; }
    public void setTxnType(TransactionType txnType) { this.txnType = txnType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getValueDate() { return valueDate; }
    public void setValueDate(LocalDate valueDate) { this.valueDate = valueDate; }

    public Long getDebitAccountId() { return debitAccountId; }
    public void setDebitAccountId(Long debitAccountId) { this.debitAccountId = debitAccountId; }

    public Long getCreditAccountId() { return creditAccountId; }
    public void setCreditAccountId(Long creditAccountId) { this.creditAccountId = creditAccountId; }
}