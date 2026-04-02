package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SettlementBatch {

    private String batchId;
    private LocalDate batchDate;
    private BatchStatus batchStatus;
    private int totalTransactions;
    private BigDecimal totalAmount;
    
    private BankType fromBank;
    private BankType toBank;
   
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public LocalDate getBatchDate() { return batchDate; }
    public void setBatchDate(LocalDate batchDate) { this.batchDate = batchDate; }

    public BatchStatus getBatchStatus() { return batchStatus; }
    public void setBatchStatus(BatchStatus batchStatus) { this.batchStatus = batchStatus; }

    public int getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(int totalTransactions) { this.totalTransactions = totalTransactions; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

   
  
    public BankType getFromBank() {
		return fromBank;
	}
	public void setFromBank(BankType frombank) {
		this.fromBank = fromBank;
	}
	public BankType getToBank() {
		return toBank;
	}
	public void setToBank(BankType toBank) {
		this.toBank = toBank;
	}
	


	public enum BatchStatus {
        RUNNING, COMPLETED, FAILED
    }

 
}