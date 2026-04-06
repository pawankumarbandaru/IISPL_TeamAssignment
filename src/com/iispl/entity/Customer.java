package com.iispl.entity;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.iispl.enums.KycStatus;

/**
 * Customer — Represents a bank customer in the settlement system.
 *
 * A Customer can own one or more Accounts.
 * KYC (Know Your Customer) status determines if the customer is allowed
 * to do transactions — compliance requirement in banking.
 *
 * IS-A BaseEntity (extends BaseEntity)
 * Inherits: id, createdAt, updatedAt, createdBy, version
 *
 * HAS-A List<Account> — COMPOSITION relationship
 *   A Customer OWNS their Accounts.
 *   In real banking: if a customer record is removed, their accounts
 *   are also removed. That is exactly what composition means —
 *   the child (Account) cannot exist without the parent (Customer).
 *
 *   Customer (1) --------< Account (many)
 *   One customer can have MANY accounts:
 *     e.g. one savings account + one current account + one NRI account
 *
 * NOTE — Two ways the relationship is maintained:
 *   1. Customer HAS-A List<Account>  ← object-level (in-memory, this file)
 *   2. Account has customerId (Long)  ← database-level (FK column in DB table)
 *   Both exist together. The List is used in Java code; the FK is used in DB queries.
 */
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // Customer's first name
    private String firstName;

    // Customer's last name
    private String lastName;

    // Customer's email address — used for notifications
    private String email;

    // KYC status — Know Your Customer verification result
    // Banking regulation requires every customer to be KYC verified
    // before they can transact. (PENDING, VERIFIED, REJECTED, EXPIRED, BLOCKED)
    private KycStatus kycStatus;

    // Customer tier — used for priority processing and fee calculation
    // Example values: "PLATINUM", "GOLD", "SILVER", "STANDARD"
    private String customerTier;

    // Date this customer was onboarded (registered) with the bank
    private LocalDate onboardingDate;

    // HAS-A COMPOSITION: A customer owns a list of bank accounts.
    // One customer → many accounts (one-to-many relationship).
    // Initialized as empty list so we never get a NullPointerException
    // when calling addAccount() on a brand new Customer object.
    private List<Account> accounts;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Default constructor.
     * KYC status starts as PENDING — new customers need to complete KYC.
     * Onboarding date is set to today by default.
     * Accounts list starts empty — accounts are added later via addAccount().
     */
    public Customer() {
        super();
        this.kycStatus      = KycStatus.PENDING;
        this.onboardingDate = LocalDate.now();
        this.accounts       = new ArrayList<>();
    }

    /**
     * Parameterized constructor — use when creating a fully populated Customer.
     *
     * @param firstName      Customer's first name
     * @param lastName       Customer's last name
     * @param email          Customer's email
     * @param kycStatus      KYC verification status
     * @param customerTier   Customer tier (PLATINUM, GOLD, SILVER etc.)
     * @param onboardingDate Date the customer was onboarded
     */
    public Customer(String firstName, String lastName, String email,
                    KycStatus kycStatus, String customerTier,
                    LocalDate onboardingDate) {
        super();
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.email          = email;
        this.kycStatus      = kycStatus;
        this.customerTier   = customerTier;
        this.onboardingDate = onboardingDate;
        this.accounts       = new ArrayList<>(); // always initialize
    }

    // -----------------------------------------------------------------------
    // HAS-A helper methods — for managing the accounts list
    // -----------------------------------------------------------------------

    /**
     * Adds an Account to this customer's list of accounts.
     *
     * IMPORTANT: This method also sets the account's customerId field
     * to keep BOTH sides of the relationship in sync:
     *   - Customer.accounts list  (object reference)
     *   - Account.customerId      (FK for database)
     *
     * Example usage:
     *   Customer customer = new Customer(...);
     *   Account savingsAcc = new Account("SB1234567890", AccountType.SAVINGS, ...);
     *   customer.addAccount(savingsAcc);  // links both ways
     *
     * @param account The Account object to link to this customer
     */
    public void addAccount(Account account) {
        if (account != null) {
            // Set the FK on the Account side so both sides stay in sync
            account.setCustomerId(this.getId());
            this.accounts.add(account);
        }
    }

    /**
     * Removes an Account from this customer's list.
     *
     * @param account The Account object to remove
     */
    public void removeAccount(Account account) {
        if (account != null) {
            this.accounts.remove(account);
        }
    }

    /**
     * Returns how many accounts this customer has.
     *
     * @return count of accounts
     */
    public int getAccountCount() {
        return this.accounts.size();
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

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

    /**
     * Convenience method — returns full name as "FirstName LastName".
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(String customerTier) {
        this.customerTier = customerTier;
    }

    public LocalDate getOnboardingDate() {
        return onboardingDate;
    }

    public void setOnboardingDate(LocalDate onboardingDate) {
        this.onboardingDate = onboardingDate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    // -----------------------------------------------------------------------
    // toString
    // -----------------------------------------------------------------------

    @Override
    public String toString() {
        return "Customer{" +
               "id=" + getId() +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", kycStatus=" + kycStatus +
               ", customerTier='" + customerTier + '\'' +
               ", onboardingDate=" + onboardingDate +
               ", accountCount=" + accounts.size() +
               '}';
    }
}

	
	
