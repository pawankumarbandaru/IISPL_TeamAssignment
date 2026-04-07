package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class DebitTransaction extends IncomingTransaction {

    public DebitTransaction(Long incomingTxnId, String sourceSystem, String sourceRef,
                            BigDecimal transactionAmount,
                            String accountNumber, Long customerId,
                            String fromBank, String channel,
                            LocalDate valueDate, TransactionStatus status,
                            LocalDateTime ingestTimestamp, String normalizedPayload) {

        super(incomingTxnId, sourceSystem, sourceRef,
              TransactionType.DEBIT, transactionAmount,
              accountNumber, customerId,
              fromBank, null, channel,
              valueDate, status,
              ingestTimestamp, normalizedPayload);
    }
}