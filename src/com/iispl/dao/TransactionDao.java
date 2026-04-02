package com.iispl.dao;

import java.util.HashSet;
import java.util.Set;

public class TransactionDao {

    private static final Set<String> refs = new HashSet<>();

    public boolean exists(String ref) {
        return refs.contains(ref);
    }

    public void save(String ref) {
        refs.add(ref);
    }
}