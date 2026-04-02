package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;

public class CreditTransaction extends Transaction {

    public CreditTransaction(Long txnId, Long creditAccountId,
                     BigDecimal amount, String currency,
                     LocalDateTime txnDate, LocalDate valueDate,
                     TransactionStatus status, String referenceNumber) {

        super(txnId, null, creditAccountId, amount, currency,
              txnDate, valueDate, status, referenceNumber);
    }
}