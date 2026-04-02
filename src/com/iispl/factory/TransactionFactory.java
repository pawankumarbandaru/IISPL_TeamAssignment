package com.iispl.factory;

import com.iispl.entity.*;
import com.iispl.enums.*;
import com.iispl.exception.InvalidTransactionTypeException;

import java.time.LocalDateTime;

public class TransactionFactory {

    public static Transaction create(IncomingTransaction in) {

        Long txnId = System.currentTimeMillis();
        
        switch (in.getTxnType()) {
            case CREDIT:
                return new CreditTransaction(txnId, in.getCreditAccountId(),
                        in.getAmount(), in.getCurrency(),
                        LocalDateTime.now(), in.getValueDate(),
                        TransactionStatus.INITIATED,
                        in.getSourceRef());

            case DEBIT:
                return new DebitTransaction(txnId, in.getDebitAccountId(),
                        in.getAmount(), in.getCurrency(),
                        LocalDateTime.now(), in.getValueDate(),
                        TransactionStatus.INITIATED,
                        in.getSourceRef());

            case INTERBANK:
                return new InterBankTransaction(txnId,
                        in.getDebitAccountId(),
                        in.getCreditAccountId(),
                        in.getAmount(), in.getCurrency(),
                        LocalDateTime.now(), in.getValueDate(),
                        TransactionStatus.INITIATED,
                        in.getSourceRef(),
                        "RBI");

            case REVERSAL:
                return new ReversalTransaction(txnId,
                        in.getDebitAccountId(),
                        in.getCreditAccountId(),
                        in.getAmount(), in.getCurrency(),
                        LocalDateTime.now(), in.getValueDate(),
                        TransactionStatus.INITIATED,
                        in.getSourceRef(),
                        in.getSourceRef());

            default:
            	throw new InvalidTransactionTypeException(String.valueOf(in.getTxnType()));        }
    }
}