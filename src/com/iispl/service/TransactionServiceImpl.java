package com.iispl.service;

<<<<<<< HEAD
import com.iispl.dao.TransactionDao;
import com.iispl.entity.*;
import com.iispl.factory.TransactionFactory;
import com.iispl.exception.*;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao dao = new TransactionDao();


=======
import com.iispl.entity.Transaction;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TransactionServiceImpl — LinkedBlockingQueue handles concurrent
 * puts from multiple threads with no synchronization needed.
 */
public class TransactionServiceImpl implements TransactionService {

    private final BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();

    @Override
    public void receive(Transaction txn) {
        try {
            queue.put(txn);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public BlockingQueue<Transaction> getQueue()        { return queue; }

    @Override
    public int                        getPendingCount() { return queue.size(); }
>>>>>>> SettlementEngine
}