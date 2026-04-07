package com.iispl.dao;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.entity.BatchRecord;
import com.iispl.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchRecordDaoImpl implements BatchRecordDao {

    @Override
    public void save(BatchRecord rec) throws SQLException {
        String sql = """
            INSERT INTO BATCH_RECORD
              (record_id, batch_id, transaction_id, transaction_type, transaction_date, amount,
               from_bank, to_bank, channel, status, value_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString   (1,  rec.getRecordId());
            ps.setString   (2,  rec.getBatchId());
            ps.setString   (3,  rec.getTxnId());
            ps.setString   (4,  rec.getTxnType().name());
            ps.setTimestamp(5, Timestamp.valueOf(rec.getDate()));
            ps.setDouble   (6,  rec.getAmount());
            ps.setString   (7,  rec.getFromBank());
            ps.setString   (8,  rec.getToBank());
            ps.setString   (9,  rec.getChannel().name());
            ps.setString   (10,  rec.getStatus().name());
            
            ps.setDate     (11, Date.valueOf(rec.getValueDate()));
            ps.executeUpdate();

            System.out.println("    [DB] Record saved → " + rec.getRecordId()
                    + "  txn=" + rec.getTxnId()
                    + "  batch=" + rec.getBatchId());
        }
    }

    @Override
    public List<BatchRecord> findByBatchId(String batchId) throws SQLException {
        String sql = "SELECT * FROM BATCH_RECORD WHERE batch_id = ? ORDER BY date";
        return query(sql, batchId);
    }

    @Override
    public List<BatchRecord> findAll() throws SQLException {
        String sql = "SELECT * FROM BATCH_RECORD ORDER BY batch_id, date";
        return query(sql, null);
    }

    private List<BatchRecord> query(String sql, String param) throws SQLException {
        List<BatchRecord> list = new ArrayList<>();

        try (Connection conn = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (param != null) ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction txn = new Transaction(
                        rs.getString("transaction_id"),
                        Transaction.TxnType.valueOf(rs.getString("transaction_type")),
                        rs.getDouble("amount"),
                        rs.getString("from_bank"),
                        rs.getString("to_bank"),
                        "N/A",
                        Transaction.Channel.valueOf(rs.getString("channel")),
                        rs.getDate("value_date").toLocalDate()
                );
                BatchRecord rec = new BatchRecord(rs.getString("batch_id"), txn);
                list.add(rec);
            }
        }
        return list;
    }
}