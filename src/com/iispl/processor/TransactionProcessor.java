package com.iispl.processor;

import com.iispl.entity.IncomingTransaction;
import com.iispl.entity.Transaction;
import com.iispl.service.TransactionService;
import com.iispl.service.TransactionServiceImpl;

public class TransactionProcessor {

    private final TransactionService service = new TransactionServiceImpl();

    public Transaction process(IncomingTransaction incoming) {

        Transaction txn = service.process(incoming);

        // ✅ Required output
        System.out.println("Transaction Created : " + txn.getReferenceNumber());

        // ✅ Print full object (DB-ready view)
        System.out.println(txn);

        return txn;
    }
}
