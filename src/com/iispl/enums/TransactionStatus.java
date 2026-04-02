package com.iispl.enums;

/**
 * Lifecycle status of a transaction from ingestion through settlement.
 *
 * Flow:
 *   RECEIVED -> VALIDATED -> QUEUED -> PROCESSING -> SETTLED
 *                         -> VALIDATION_FAILED
 *                                              -> FAILED -> DEAD_LETTER
 *                                              -> REVERSED
 *                                              -> ON_HOLD
 */
public enum TransactionStatus {
    RECEIVED,           // Raw record just read from the source file
    VALIDATED,          // Passed all field-level validation checks
    VALIDATION_FAILED,  // Failed validation; will not proceed further
    QUEUED,             // Placed onto BlockingQueue for settlement processing
    PROCESSING,         // Currently being handled by SettlementProcessor
    SETTLED,            // Successfully settled
    PARTIALLY_SETTLED,  // Some legs settled, others still pending
    FAILED,             // Processing failed; may be retried
    REVERSED,           // A previously settled txn has been reversed
    ON_HOLD,            // Flagged for manual review
    DEAD_LETTER         // Exhausted all retries; parked for investigation
}
