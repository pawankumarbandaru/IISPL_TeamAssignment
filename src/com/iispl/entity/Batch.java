package com.iispl.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Batch {

    public enum BatchStatus { OPEN, COMPLETED, FAILED }

    private final String batchId;
    private final String fromBank;
    private final String toBank;

    private int totalTxns;
    private double totalAmount;

    private final LocalDate date;
    private final LocalDateTime createdAt;
    private BatchStatus status;

    private List<Transaction> transactions;

    public Batch(String batchId, String fromBank, String toBank, LocalDate date) {
        this.batchId = batchId;
        this.fromBank = fromBank;
        this.toBank = toBank;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.status = BatchStatus.OPEN;

        this.transactions = new ArrayList<>();   // ✅ IMPORTANT
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTotalTxns(int totalTxns) { this.totalTxns = totalTxns; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getBatchId() { return batchId; }
    public String getFromBank() { return fromBank; }
    public String getToBank() { return toBank; }
    public int getTotalTxns() { return totalTxns; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDate getDate() { return date; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BatchStatus getStatus() { return status; }

    public void setStatus(BatchStatus status) { this.status = status; }
}