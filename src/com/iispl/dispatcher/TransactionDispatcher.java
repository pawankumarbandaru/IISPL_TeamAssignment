package com.iispl.dispatcher;

import java.util.concurrent.BlockingQueue;

import com.iispl.entity.IncomingTransaction;
import com.iispl.entity.Transaction;
import com.iispl.factory.TransactionFactory;
import com.iispl.queue.TransactionQueue;


public class TransactionDispatcher implements Runnable {
	
	private TransactionQueue transactionQueue;

    private BlockingQueue<Transaction> creditQueue;
    private BlockingQueue<Transaction> debitQueue;
    private BlockingQueue<Transaction> interBankQueue;
    private BlockingQueue<Transaction> reversalQueue;

    public TransactionDispatcher(
            TransactionQueue transactionQueue,
            BlockingQueue<Transaction> creditQueue,
            BlockingQueue<Transaction> debitQueue,
            BlockingQueue<Transaction> interBankQueue,
            BlockingQueue<Transaction> reversalQueue) {

        this.transactionQueue = transactionQueue;
        this.creditQueue = creditQueue;
        this.debitQueue = debitQueue;
        this.interBankQueue = interBankQueue;
        this.reversalQueue = reversalQueue;
    }
    
    @Override
    public void run() {

        while (true) {
            try {
                // 1. Take incoming transaction (blocks if empty)
                IncomingTransaction incoming = transactionQueue.take();

                // 2. Convert to domain object
                Transaction txn = TransactionFactory.convert(incoming);

                // 3. Dispatch based on type
                switch (txn.getTxnType()) {

                    case CREDIT:
                        creditQueue.put(txn);
                        break;

                    case DEBIT:
                        debitQueue.put(txn);
                        break;

                    case INTERBANK:
                        interBankQueue.put(txn);
                        break;

                    case REVERSAL:
                        reversalQueue.put(txn);
                        break;

                    default:
                        System.err.println("Unknown txn type: " + txn.getTxnType());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Dispatcher interrupted");
                break;
            } catch (Exception e) {
                System.err.println("Error processing transaction: " + e.getMessage());
            }
        }
    }
}