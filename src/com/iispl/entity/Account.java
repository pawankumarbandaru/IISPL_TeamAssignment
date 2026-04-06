package com.iispl.entity;

import com.iispl.enums.AccountStatus;
import com.iispl.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account — Represents a bank account in the settlement system.
 *
 * Examples of accounts:
 *   - A customer's savings account
 *   - A NOSTRO account our bank holds at a foreign bank
 *   - A VOSTRO account a foreign bank holds at our bank
 *   - A suspense account for unmatched entries
 *
 * IS-A BaseEntity (extends BaseEntity)
 * Inherits: id, createdAt, updatedAt, createdBy, version
 *
 * IMPORTANT — Thread Safety Note (for T4 teammate):
 *   The updateBalance() method is synchronized. This means only one thread
 *   can update the balance at a time. This prevents two settlement threads
 *   from corrupting the balance simultaneously.
 */
public class Account extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // Human-readable account number e.g. "SB1234567890", "CA9876543210"
    private String accountNumber;

    // What kind of account is this? (SAVINGS, CURRENT, NOSTRO, VOSTRO etc.)
    private AccountType accountType;

    // Current balance — ALWAYS BigDecimal for financial values
    // synchronized access via updateBalance() method
    private BigDecimal balance;

    // ISO 4217 currency code for this account e.g. "INR", "USD"
    private String currency;

    // Foreign key linking this account to its owner Customer
    private Long customerId;

    // Foreign key linking this account to the bank it belongs to
    private Long bankId;

    // Is this account ACTIVE, FROZEN, CLOSED etc.?
    private AccountStatus status;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Default constructor.
     * Balance starts at ZERO for new accounts.
     * Status starts as ACTIVE.
     */
    public Account() {
        super();
        this.balance = BigDecimal.ZERO;
        this.status  = AccountStatus.ACTIVE;
    }

    /**
     * Parameterized constructor — use when creating a fully populated Account.
     *
     * @param accountNumber Account number string e.g. "SB1234567890"
     * @param accountType   Type of account (SAVINGS, CURRENT, NOSTRO etc.)
     * @param balance       Opening balance (BigDecimal)
     * @param currency      ISO currency code
     * @param customerId    FK to Customer table
     * @param bankId        FK to Bank table
     */
    public Account(String accountNumber, AccountType accountType,
                   BigDecimal balance, String currency,
                   Long customerId, Long bankId) {
        super();
        this.accountNumber = accountNumber;
        this.accountType   = accountType;
        this.balance       = balance;
        this.currency      = currency;
        this.customerId    = customerId;
        this.bankId        = bankId;
        this.status        = AccountStatus.ACTIVE;
    }

    // -----------------------------------------------------------------------
    // Synchronized balance update — Thread-Safe
    // -----------------------------------------------------------------------

    /**
     * Updates the account balance in a thread-safe way.
     *
     * WHY SYNCHRONIZED?
     *   The settlement engine runs multiple threads at the same time.
     *   If two threads try to update the same account balance at the same moment,
     *   the result will be wrong (this is called a "race condition").
     *   The 'synchronized' keyword ensures only one thread can run this
     *   method at a time on the same Account object.
     *
     * @param newBalance The new balance to set for this account
     */
    public synchronized void updateBalance(BigDecimal newBalance) {
        this.balance = newBalance;
        // Always update the timestamp when data changes
        setUpdatedAt(LocalDateTime.now());
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    // -----------------------------------------------------------------------
    // toString
    // -----------------------------------------------------------------------

    @Override
    public String toString() {
        return "Account{" +
               "accountNumber='" + accountNumber + '\'' +
               ", accountType=" + accountType +
               ", balance=" + balance +
               ", currency='" + currency + '\'' +
               ", customerId=" + customerId +
               ", bankId=" + bankId +
               ", status=" + status +
               '}';
    }
}