package com.iispl.service;

import com.iispl.entity.Batch;
import com.iispl.entity.BatchRecord;
import com.iispl.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * BatchService — sits between BatchProcessor and DAO layer.
 */
public interface BatchService {

    void              saveBatch(Batch batch)                        throws SQLException;
    BatchRecord       createAndSaveRecord(Batch batch, Transaction txn) throws SQLException;
    List<Batch>       getAllBatches()                               throws SQLException;
    List<BatchRecord> getAllRecords()                               throws SQLException;
    List<BatchRecord> getRecordsByBatch(String batchId)            throws SQLException;
}