package com.iispl.service;

<<<<<<< HEAD
import com.iispl.entity.IncomingTransaction;
import com.iispl.entity.Transaction;

public interface TransactionService {

  
=======

import com.iispl.entity.Transaction;
import java.util.concurrent.BlockingQueue;

/**
 * TransactionService — thread-safe receive layer.
 * Multiple threads call receive() simultaneously.
 */
public interface TransactionService {
    void                       receive(Transaction txn);
    BlockingQueue<Transaction> getQueue();
    int                        getPendingCount();
>>>>>>> SettlementEngine
}