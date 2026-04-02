package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;

public class InterBankTransaction extends Transaction {

    private final String correspondent;

    public InterBankTransaction(Long txnId, Long debitAccountId, Long creditAccountId,
                        BigDecimal amount, String currency,
                        LocalDateTime txnDate, LocalDate valueDate,
                        TransactionStatus status, String referenceNumber,
                        String correspondent) {

        super(txnId, debitAccountId, creditAccountId, amount, currency,
              txnDate, valueDate, status, referenceNumber);

        this.correspondent = correspondent;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ",\n  correspondent='" + correspondent + '\'' +
                "\n}";
    }
}