package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.iispl.enums.BankName;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class InterBankTransaction extends Transaction {

    private final String correspondent;

    public InterBankTransaction(UUID txnId, BigDecimal amount, 
            TransactionType txnType, TransactionStatus txnStatus, 
            BankName fromBank, BankName toBank, String channel, 
            LocalDateTime txnDateTime, LocalDateTime valueDateTime,
            String correspondent) 
    {
        super(txnId, amount, txnType, txnStatus, fromBank, 
              toBank, channel, txnDateTime, valueDateTime);
        this.correspondent = correspondent;
    }

    public String getCorrespondent() {
        return correspondent;
    }
}