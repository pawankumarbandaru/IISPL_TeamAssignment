package com.iispl.enums;


/**
 * Classifies the nature / direction of a transaction.
 */
public enum TransactionType {
    CREDIT,     // Money coming into an account
    DEBIT,      // Money going out of an account
    REVERSAL,   // Reverses a previously settled transaction
    SWAP,       // Currency swap between two positions
    FEE,        // Bank charge or fee
    INTRABANK   // Transfer within the same bank (no external rail)
}
