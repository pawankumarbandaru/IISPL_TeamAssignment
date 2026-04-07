package com.iispl.dao;

import com.iispl.entity.BatchRecord;
import java.sql.SQLException;
import java.util.List;

public interface BatchRecordDao {
    void              save(BatchRecord record)       throws SQLException;
    List<BatchRecord> findByBatchId(String batchId)  throws SQLException;
    List<BatchRecord> findAll()                      throws SQLException;
}