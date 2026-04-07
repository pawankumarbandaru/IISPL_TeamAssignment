package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class InterBankTransaction extends IncomingTransaction {

    private final String correspondent;

    public InterBankTransaction(Long incomingTxnId, String sourceSystem, String sourceRef,
                                BigDecimal transactionAmount,
                                String accountNumber, Long customerId,
                                String fromBank, String toBank,
                                String channel,
                                LocalDate valueDate, TransactionStatus status,
                                LocalDateTime ingestTimestamp, String normalizedPayload,
                                String correspondent) {

        super(incomingTxnId, sourceSystem, sourceRef,
              TransactionType.INTERBANK, transactionAmount,
              accountNumber, customerId,
              fromBank, toBank, channel,
              valueDate, status,
              ingestTimestamp, normalizedPayload);

        this.correspondent = correspondent;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ",\n  correspondent='" + correspondent + '\'' +
                "\n}";
    }
}