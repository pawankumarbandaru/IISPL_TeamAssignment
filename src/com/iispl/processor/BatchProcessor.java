package com.iispl.processor;

import com.iispl.entity.Batch;
import com.iispl.entity.Transaction;
import com.iispl.service.BatchService;
import com.iispl.service.TransactionService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchProcessor {

    private final TransactionService txnService;
    private final BatchService batchService;
    private final AtomicInteger batchCounter = new AtomicInteger(1);

    private final Map<String, Batch> openBatches = new LinkedHashMap<>();
    private final List<Batch> completedBatches = new ArrayList<>();

    public BatchProcessor(TransactionService txnService, BatchService batchService) {
        this.txnService = txnService;
        this.batchService = batchService;
    }

    public void drainAndGroup() {

        BlockingQueue<Transaction> queue = txnService.getQueue();

        while (!queue.isEmpty()) {
            Transaction txn = queue.poll();
            if (txn == null) break;

            String key = txn.getBatchKey();

            Batch batch = openBatches.computeIfAbsent(key, k ->
                    new Batch(
                            buildBatchId(txn.getFromBank(), txn.getToBank(), txn.getValueDate()),
                            txn.getFromBank(),
                            txn.getToBank(),
                            txn.getValueDate()
                    )
            );

            // ✅ ADD txn to list
            batch.getTransactions().add(txn);

            // ✅ UPDATE totals IMMEDIATELY (THIS IS THE FIX)
            batch.setTotalTxns(batch.getTotalTxns() + 1);
            batch.setTotalAmount(batch.getTotalAmount() + txn.getAmount());
        }
    }

    public void sealAllBatches() throws SQLException {

        for (Batch batch : openBatches.values()) {

            batch.setStatus(Batch.BatchStatus.COMPLETED);

            // ✅ totals already set → just save
            batchService.saveBatch(batch);

            completedBatches.add(batch);
        }

        openBatches.clear();
    }
    // STEP 3
    public void createAllRecords() throws SQLException {
        for (Batch batch : completedBatches) {
            for (Transaction txn : batch.getTransactions()) {
                batchService.createAndSaveRecord(batch, txn);
            }
        }
    }

    public void process() throws SQLException {
        drainAndGroup();
        sealAllBatches();
        createAllRecords();
    }

    private String buildBatchId(String fromBank, String toBank, LocalDate date) {
        return String.format("BTCH-%03d-%s-%s-%s",
                batchCounter.getAndIncrement(),
                abbr(fromBank),
                abbr(toBank),
                date.toString().replace("-", ""));
    }

    private String abbr(String name) {
        String w = name.split("\\s+")[0];
        return w.substring(0, Math.min(4, w.length())).toUpperCase();
    }
}