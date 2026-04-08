
package com.iispl.utility;

import com.iispl.dao.BatchDaoImpl;
import com.iispl.dao.BatchRecordDaoImpl;
import com.iispl.entity.Batch;
import com.iispl.entity.BatchRecord;
import com.iispl.entity.Transaction;
import com.iispl.entity.Transaction.Channel;
import com.iispl.entity.Transaction.TxnType;
import com.iispl.processor.BatchProcessor;
import com.iispl.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // Disable c3p0 logs
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");

        // Wiring
        TransactionService txnService = new TransactionServiceImpl();
        BatchService batchService = new BatchServiceImpl(
                new BatchDaoImpl(),
                new BatchRecordDaoImpl()
        );
        BatchProcessor processor = new BatchProcessor(txnService, batchService);

        LocalDate d1 = LocalDate.of(2026, 4, 6);
        LocalDate d2 = LocalDate.of(2026, 4, 7);

        
     // ===========================
     // STEP 1: FILL QUEUES (simulate teammates)
     // ===========================

     QueueManager.creditQueue.add(txn("CREDIT", 50000, "HDFC", "SBI", "NEFT", d1));
     QueueManager.creditQueue.add(txn("CREDIT", 45000, "HDFC", "SBI", "NEFT", d2));

     QueueManager.debitQueue.add(txn("DEBIT", 30000, "HDFC", "SBI", "RTGS", d1));
     QueueManager.debitQueue.add(txn("DEBIT", 75000, "ICICI", "AXIS BANK", "RTGS", d1));

     QueueManager.reversalQueue.add(txn("REVERSAL", 20000, "SBI", "HDFC", "IMPS", d1));
     QueueManager.interbank.add(txn("REVERSAL", 10000, "KOTAK", "SBI", "UPI", d1));


     // ===========================
     // STEP 2: CONSUME USING THREAD POOL
     // ===========================

     ExecutorService executor = Executors.newFixedThreadPool(4);

     executor.submit(new QueueConsumer(QueueManager.creditQueue, txnService));
     executor.submit(new QueueConsumer(QueueManager.debitQueue, txnService));
     executor.submit(new QueueConsumer(QueueManager.reversalQueue, txnService));
     executor.submit(new QueueConsumer(QueueManager.interbank, txnService));

     executor.shutdown();
     executor.awaitTermination(5, TimeUnit.SECONDS);

        // ===========================
        // STEP 2: PROCESS BATCHES
        // ===========================
        processor.process();

        // ===========================
        // STEP 3: READ FROM DB
        // ===========================
        List<Batch> batches = batchService.getAllBatches();
        List<BatchRecord> records = batchService.getAllRecords();

        // ===========================
        // PRINT BATCH TABLE
        // ===========================
        System.out.println("\n--------------------------------------------------------------------------------------------");
        System.out.printf("%-30s | %-12s | %-12s | %-10s | %-12s | %-12s  | %-10s%n",
                "batch_id", "from_bank", "to_bank", "total_transactions", "total_amount", "transaction_date", "status");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Batch b : batches) {
            System.out.printf("%-30s | %-12s | %-12s | %-10d | %-12.0f | %-12s  | %-10s%n",
                    b.getBatchId(),
                    b.getFromBank(),
                    b.getToBank(),
                    b.getTotalTxns(),
                    b.getTotalAmount(),
                    b.getDate(),
                    b.getStatus());
        }

        // ===========================
        // PRINT RECORD TABLE
        // ===========================
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-30s | %-12s | %-10s | %-10s | %-12s | %-12s | %-8s | %-10s | %-12s%n",
                "record_id", "batch_id", "transaction_id", "transaction_type", "amount",
                "from_bank", "to_bank", "channel", "status", "value_date");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        for (BatchRecord r : records) {
            System.out.printf("%-12s | %-30s | %-12s | %-10s | %-10.0f | %-12s | %-12s | %-8s | %-10s | %-12s%n",
                    r.getRecordId(),
                    r.getBatchId(),
                    r.getTxnId(),
                    r.getTxnType(),
                    r.getAmount(),
                    r.getFromBank(),
                    r.getToBank(),
                    r.getChannel(),
                    r.getStatus(),
                    r.getValueDate());
        }
    }

    // Receive with thread name (shows concurrency)
    static void receive(TransactionService svc, Transaction t) {
        svc.receive(t);
        System.out.println(Thread.currentThread().getName() + " → " + t.getTxnType() + " → " + t.getTxnId());
    }

    // Helper method
    static Transaction txn(String type, double amount, String from,
                           String to, String channel, LocalDate date) {
        return new Transaction(
                "TXN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
                TxnType.valueOf(type),
                amount,
                from,
                to,
                "ACC-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase(),
                Channel.valueOf(channel),
                date
        );
    }
}
