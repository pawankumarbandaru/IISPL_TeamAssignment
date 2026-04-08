package com.iispl.factory;

import com.iispl.enums.BankName;
import com.iispl.entity.CreditTransaction;
import com.iispl.entity.DebitTransaction;
import com.iispl.entity.IncomingTransaction;
import com.iispl.entity.InterBankTransaction;
import com.iispl.entity.ReversalTransaction;
import com.iispl.entity.Transaction;
import com.iispl.enums.TransactionStatus;


public class TransactionFactory {

    public static Transaction convert(IncomingTransaction incoming) {

        if (incoming == null) {
            throw new IllegalArgumentException("IncomingTransaction cannot be null");
        }

        switch (incoming.getTxnType()) {

            case CREDIT:
                return new CreditTransaction(
                    incoming.getIncomingTxnId(),
                    incoming.getTransactionAmount(),
                    incoming.getTxnType(),
                    mapStatus(incoming.getStatus()),
                    mapBank(incoming.getFromBank()),
                    mapBank(incoming.getToBank()),
                    incoming.getChannel(),
                    incoming.getTxnDateTime(),
                    incoming.getValueDateTime()
                );

            case DEBIT:
                return new DebitTransaction(
                    incoming.getIncomingTxnId(),
                    incoming.getTransactionAmount(),
                    incoming.getTxnType(),
                    mapStatus(incoming.getStatus()),
                    mapBank(incoming.getFromBank()),
                    mapBank(incoming.getToBank()),
                    incoming.getChannel(),
                    incoming.getTxnDateTime(),
                    incoming.getValueDateTime()
                );

            case INTERBANK:
                return new InterBankTransaction(
                    incoming.getIncomingTxnId(),
                    incoming.getTransactionAmount(),
                    incoming.getTxnType(),
                    mapStatus(incoming.getStatus()),
                    mapBank(incoming.getFromBank()),
                    mapBank(incoming.getToBank()),
                    incoming.getChannel(),
                    incoming.getTxnDateTime(),
                    incoming.getValueDateTime(),
                    incoming.getCorrespondent() 
                );

            case REVERSAL:
                return new ReversalTransaction(
                    incoming.getIncomingTxnId(),
                    incoming.getTransactionAmount(),
                    incoming.getTxnType(),
                    mapStatus(incoming.getStatus()),
                    mapBank(incoming.getFromBank()),
                    mapBank(incoming.getToBank()),
                    incoming.getChannel(),
                    incoming.getTxnDateTime(),
                    incoming.getValueDateTime(),
                    incoming.getOriginalRef()
                );

            default:
                throw new IllegalArgumentException("Unsupported txn type");
        }
    }

    private static TransactionStatus mapStatus(TransactionStatus status) {
        return (status == null) ? TransactionStatus.PENDING : status;
    }

    private static BankName mapBank(String bank) {
        if (bank == null) return null;
        try {
            return BankName.valueOf(bank.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
},