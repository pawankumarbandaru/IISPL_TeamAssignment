package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class ReversalTransaction extends IncomingTransaction {

    private final String originalRef;

    public ReversalTransaction(Long incomingTxnId, String sourceSystem, String sourceRef,
                               BigDecimal transactionAmount,
                               String accountNumber, Long customerId,
                               String fromBank, String toBank,
                               String channel,
                               LocalDate valueDate, TransactionStatus status,
                               LocalDateTime ingestTimestamp, String normalizedPayload,
                               String originalRef) {

        super(incomingTxnId, sourceSystem, sourceRef,
              TransactionType.REVERSAL, transactionAmount,
              accountNumber, customerId,
              fromBank, toBank, channel,
              valueDate, status,
              ingestTimestamp, normalizedPayload);

        this.originalRef = originalRef;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ",\n  originalRef='" + originalRef + '\'' +
                "\n}";
    }
}