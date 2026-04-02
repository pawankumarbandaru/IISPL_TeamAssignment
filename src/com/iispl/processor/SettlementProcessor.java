package com.iispl.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.SettlementBatchDAO;
import com.iispl.dao.SettlementBatchDAOImpl;
import com.iispl.entity.BankType;
import com.iispl.entity.InterBankTxn;
import com.iispl.entity.SettlementBatch;
import com.iispl.entity.Transaction;
import com.iispl.entity.Transaction.TransactionStatus;
import com.iispl.service.SettlementBatchService;
import com.iispl.service.SettlementBatchServiceImpl;

public class SettlementProcessor {

    private List<Transaction> buffer = new ArrayList<>();

    private SettlementBatchService batchService = new SettlementBatchServiceImpl();

    // Receive transactions one by one
    public void process(Transaction txn) {
        buffer.add(txn);

        // Trigger batch when 3 transactions come
        if (buffer.size() >= 3) {
            triggerBatch();
        }
    }

    private void triggerBatch() {

        // Copy buffer
        List<Transaction> txns = new ArrayList<>(buffer);
        buffer.clear();

        // Create batches
        List<SettlementBatch> batches = batchService.createBatches(txns);

        SettlementBatchDAO batchDAO = new SettlementBatchDAOImpl();

        // Save batches
        for (SettlementBatch batch : batches) {
            try {
                batchDAO.save(batch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //  PRINT ALL BATCHES IN ONE TABLE
        System.out.println("\n================ BATCH SUMMARY ================");
        System.out.printf("%-40s %-15s %-15s %-15s %-10s %-10s%n",
                "BatchId", "Status", "TotalTxns", "TotalAmount", "From", "To");

        for (SettlementBatch batch : batches) {
            System.out.printf("%-40s %-15s %-15d %-15s %-10s %-10s%n",
                    batch.getBatchId(),
                    batch.getBatchStatus(),
                    batch.getTotalTransactions(),
                    batch.getTotalAmount(),
                    batch.getFromBank(),
                    batch.getToBank());
        }

        System.out.println("================================================");
    }

    // MAIN METHOD WITH ENUM DATA
    public static void main(String[] args) {

        SettlementProcessor processor = new SettlementProcessor();

        Transaction t1 = new InterBankTxn();
        t1.setTxnId(1L);
        t1.setFromBank(BankType.HDFC);
        t1.setToBank(BankType.SBI);
        t1.setAmount(new BigDecimal("1000"));
        t1.setCurrency("INR");
        t1.setValueDate(LocalDate.now());
        t1.setStatus(TransactionStatus.VALIDATED);

        Transaction t2 = new InterBankTxn();
        t2.setTxnId(2L);
        t2.setFromBank(BankType.HDFC);
        t2.setToBank(BankType.SBI);
        t2.setAmount(new BigDecimal("500"));
        t2.setCurrency("INR");
        t2.setValueDate(LocalDate.now());
        t2.setStatus(TransactionStatus.VALIDATED);

        Transaction t3 = new InterBankTxn();
        t3.setTxnId(3L);
        t3.setFromBank(BankType.SBI);
        t3.setToBank(BankType.HDFC);
        t3.setAmount(new BigDecimal("700"));
        t3.setCurrency("INR");
        t3.setValueDate(LocalDate.now());
        t3.setStatus(TransactionStatus.VALIDATED);

        processor.process(t1);
        processor.process(t2);
        processor.process(t3);
    }
}