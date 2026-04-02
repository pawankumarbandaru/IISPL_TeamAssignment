package com.iispl.utility;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.entity.IncomingTransaction;
import com.iispl.enums.TransactionType;
import com.iispl.processor.TransactionProcessor;

public class TransactionUtility {

    public static void main(String[] args) {

        IncomingTransaction in = new IncomingTransaction();

        in.setSourceRef("TXN1001");
        in.setTxnType(TransactionType.INTERBANK);
        in.setAmount(new BigDecimal("5000"));
        in.setCurrency("INR");
        in.setValueDate(LocalDate.now());
        in.setDebitAccountId(101L);
        in.setCreditAccountId(202L);

        TransactionProcessor processor = new TransactionProcessor();

        try {
            var txn = processor.process(in);
           // System.out.println("Transaction Created: " + txn.getReferenceNumber());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}