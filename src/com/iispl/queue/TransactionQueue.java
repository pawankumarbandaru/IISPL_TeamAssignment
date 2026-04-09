package com.iispl.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.iispl.entity.IncomingTransaction;

public class TransactionQueue {

    private final BlockingQueue<IncomingTransaction> queue;

    public TransactionQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public void put(IncomingTransaction txn) throws InterruptedException {
        queue.put(txn);
    }

    public IncomingTransaction take() throws InterruptedException {
        return queue.take();
    }
}