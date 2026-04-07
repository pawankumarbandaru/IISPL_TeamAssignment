package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class CreditTransaction extends IncomingTransaction {

    public CreditTransaction(Long incomingTxnId, String sourceSystem, String sourceRef,
                             BigDecimal transactionAmount,
                             String accountNumber, Long customerId,
                             String toBank, String channel,
                             LocalDate valueDate, TransactionStatus status,
                             LocalDateTime ingestTimestamp, String normalizedPayload) {

        super(incomingTxnId, sourceSystem, sourceRef,
              TransactionType.CREDIT, transactionAmount,
              accountNumber, customerId,
              null, toBank, channel,
              valueDate, status,
              ingestTimestamp, normalizedPayload);
    }
}