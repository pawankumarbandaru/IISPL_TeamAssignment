package com.iispl.entity;

import java.util.List;

public class Customer {

	protected String firstName;
	protected String lastName;
	protected String email;
	protected String kycStatus;
	protected String customerTier;
	protected String onboardingDate;
	protected String customerId;
	protected List<Account> accountList;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getKycStatus() {
		return kycStatus;
	}
	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}
	public String getCustomerTier() {
		return customerTier;
	}
	public void setCustomerTier(String customerTier) {
		this.customerTier = customerTier;
	}
	public String getOnboardingDate() {
		return onboardingDate;
	}
	public void setOnboardingDate(String onboardingDate) {
		this.onboardingDate = onboardingDate;
	}
	public List<Account> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public Customer(String firstName, String lastName, String email, String kycStatus, String customerTier,
			String onboardingDate, String customerId, List<Account> accountList) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.kycStatus = kycStatus;
		this.customerTier = customerTier;
		this.onboardingDate = onboardingDate;
		this.customerId = customerId;
		this.accountList = accountList;
	}
	
	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", kycStatus="
				+ kycStatus + ", customerTier=" + customerTier + ", onboardingDate=" + onboardingDate + ", customerId="
				+ customerId + ", accountList=" + accountList + "]";
	}
	
	
	
}
