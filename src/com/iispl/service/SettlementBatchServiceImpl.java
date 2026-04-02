package com.iispl.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.iispl.entity.BankType;
import com.iispl.entity.SettlementBatch;
import com.iispl.entity.SettlementBatch.BatchStatus;
import com.iispl.entity.Transaction;

public class SettlementBatchServiceImpl implements SettlementBatchService {

    @Override
    public List<SettlementBatch> createBatches(List<Transaction> transactions) {

        //  1. Group by fromBank + toBank
    	Map<String, List<Transaction>> grouped =
    	        transactions.stream()
    	                .collect(Collectors.groupingBy(
    	                        txn -> txn.getFromBank() + "_" + txn.getToBank()
    	                ));

    	List<SettlementBatch> batches = new ArrayList<>();

    	

        // 2. Create batch for each group
        for (Map.Entry<String, List<Transaction>> entry : grouped.entrySet()) {

            List<Transaction> txnList = entry.getValue();

            SettlementBatch batch = new SettlementBatch();
            
            String[] parts = entry.getKey().split("_");

            batch.setFromBank(BankType.valueOf(parts[0]));
            batch.setToBank(BankType.valueOf(parts[1]));


            batch.setBatchId(UUID.randomUUID().toString());
            batch.setBatchDate(LocalDate.now());
            batch.setBatchStatus(BatchStatus.COMPLETED);

            //calculate total
            BigDecimal total = txnList.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            batch.setTotalTransactions(txnList.size());
            batch.setTotalAmount(total);

            batches.add(batch);
        }

        return batches;
    }
}