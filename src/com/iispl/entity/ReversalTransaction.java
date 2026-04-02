package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;

public class ReversalTransaction extends Transaction {

    private final String originalRef;

    public ReversalTransaction(Long txnId, Long debitAccountId, Long creditAccountId,
                       BigDecimal amount, String currency,
                       LocalDateTime txnDate, LocalDate valueDate,
                       TransactionStatus status, String referenceNumber,
                       String originalRef) {

        super(txnId, debitAccountId, creditAccountId, amount, currency,
              txnDate, valueDate, status, referenceNumber);

        this.originalRef = originalRef;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ",\n  originalRef='" + originalRef + '\'' +
                "\n}";
    }
}