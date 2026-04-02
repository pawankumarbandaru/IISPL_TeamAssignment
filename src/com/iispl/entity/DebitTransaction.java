package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;

public class DebitTransaction extends Transaction {

    public DebitTransaction(Long txnId, Long debitAccountId,
                    BigDecimal amount, String currency,
                    LocalDateTime txnDate, LocalDate valueDate,
                    TransactionStatus status, String referenceNumber) {

        super(txnId, debitAccountId, null, amount, currency,
              txnDate, valueDate, status, referenceNumber);
    }
}