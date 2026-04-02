package com.iispl.entity;

import java.math.BigDecimal;

public abstract class Account {

	protected String accountNumber;
	protected String accountType;
	protected BigDecimal balance;
	protected String bankId;
	protected String status;
	
	public Account(String accountNumber, String accountType, BigDecimal balance, String bankId, String status) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.bankId = bankId;
		this.status = status;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getBalance() {
		return balance;
	}

	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountType=" + accountType + ", balance=" + balance
				+ ", bankId=" + bankId + ", status=" + status + "]";
	}
	
}
