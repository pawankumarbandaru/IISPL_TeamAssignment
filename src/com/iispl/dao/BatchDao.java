package com.iispl.dao;

import com.iispl.entity.Batch;
import java.sql.SQLException;
import java.util.List;

public interface BatchDao {
    void        save(Batch batch) throws SQLException;
    List<Batch> findAll()         throws SQLException;
}