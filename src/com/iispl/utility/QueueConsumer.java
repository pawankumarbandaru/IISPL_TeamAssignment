package com.iispl.utility;

import com.iispl.entity.Transaction;
import com.iispl.service.TransactionService;

import java.util.concurrent.BlockingQueue;

public class QueueConsumer implements Runnable {

    private final BlockingQueue<Transaction> queue;
    private final TransactionService txnService;

    public QueueConsumer(BlockingQueue<Transaction> queue, TransactionService txnService) {
        this.queue = queue;
        this.txnService = txnService;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            Transaction txn = queue.poll();
            if (txn != null) {
                txnService.receive(txn);
                System.out.println(Thread.currentThread().getName()
                        + " → consumed → " + txn.getTxnId());
            }
        }
    }
}