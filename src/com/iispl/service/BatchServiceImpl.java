package com.iispl.service;

import com.iispl.dao.BatchDao;
import com.iispl.dao.BatchRecordDao;
import com.iispl.entity.Batch;
import com.iispl.entity.BatchRecord;
import com.iispl.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * BatchServiceImpl — delegates all DB operations to DAOs.
 * BatchDao and BatchRecordDao injected via constructor.
 */
public class BatchServiceImpl implements BatchService {

    private final BatchDao       batchDao;
    private final BatchRecordDao recordDao;

    public BatchServiceImpl(BatchDao batchDao, BatchRecordDao recordDao) {
        this.batchDao  = batchDao;
        this.recordDao = recordDao;
    }

    @Override
    public void saveBatch(Batch batch) throws SQLException {
        batchDao.save(batch);
    }

    @Override
    public BatchRecord createAndSaveRecord(Batch batch, Transaction txn) throws SQLException {
        BatchRecord record = new BatchRecord(batch.getBatchId(), txn);
        recordDao.save(record);
        return record;
    }

    @Override
    public List<Batch> getAllBatches() throws SQLException {
        return batchDao.findAll();
    }

    @Override
    public List<BatchRecord> getAllRecords() throws SQLException {
        return recordDao.findAll();
    }

    @Override
    public List<BatchRecord> getRecordsByBatch(String batchId) throws SQLException {
        return recordDao.findByBatchId(batchId);
    }
}