package com.iispl.dao;

import com.iispl.connectionpool.ConnectionPool;
import com.iispl.entity.Batch;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchDaoImpl implements BatchDao {

    @Override
    public void save(Batch batch) throws SQLException {
        String sql = """
            INSERT INTO BATCH
              (batch_id, from_bank, to_bank, total_transactions, total_amount, transaction_date , status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // Borrow connection from c3p0 pool → auto-returned when try-with-resources closes it
        try (Connection conn = ConnectionPool.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString   (1, batch.getBatchId());
            ps.setString   (2, batch.getFromBank());
            ps.setString   (3, batch.getToBank());
            ps.setInt      (4, batch.getTotalTxns());
            ps.setDouble   (5, batch.getTotalAmount());
            ps.setDate     (6, Date.valueOf(batch.getDate()));
            
            ps.setString   (7, batch.getStatus().name());
            ps.executeUpdate();

            System.out.println("  [DB] Batch saved → " + batch.getBatchId());
        }
    }

    @Override
    public List<Batch> findAll() throws SQLException {
        String      sql  = "SELECT * FROM BATCH ORDER BY created_at";
        List<Batch> list = new ArrayList<>();

        try (Connection conn = ConnectionPool.getDataSource().getConnection();
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
            	Batch b = new Batch(
            	        rs.getString("batch_id"),
            	        rs.getString("from_bank"),
            	        rs.getString("to_bank"),
            	        rs.getDate("date").toLocalDate()
            	);

            	// ✅ ADD THESE 2 LINES (THIS IS YOUR FIX)
            	b.setTotalTxns(rs.getInt("total_transactions"));
            	b.setTotalAmount(rs.getDouble("total_amount"));

            	// already there
            	b.setStatus(Batch.BatchStatus.valueOf(rs.getString("status")));
            	list.add(b); 
            }
        }
        return list;
    }
    
}