package com.iispl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.entity.SettlementBatch;

public class SettlementBatchDAOImpl implements SettlementBatchDAO {

    @Override
    public void save(SettlementBatch batch) throws Exception {

        String sql = "INSERT INTO settlement_batch (batch_id, batch_date, batch_status, total_transactions, total_amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, batch.getBatchId());
            ps.setDate(2, java.sql.Date.valueOf(batch.getBatchDate()));
            ps.setString(3, batch.getBatchStatus().name());
            ps.setInt(4, batch.getTotalTransactions());
            ps.setBigDecimal(5, batch.getTotalAmount());

            ps.executeUpdate();
        }
    }
}