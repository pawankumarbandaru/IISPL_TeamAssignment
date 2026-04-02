package com.iispl.service;

import java.util.List;
import com.iispl.entity.Transaction;
import com.iispl.entity.SettlementBatch;
public interface SettlementBatchService {
    List<SettlementBatch> createBatches(List<Transaction> transactions);
}