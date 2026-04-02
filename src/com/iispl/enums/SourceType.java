package com.iispl.enums;



/**
 * Identifies which source system sent the transaction.
 * Each value maps to one adapter implementation.
 *
 *   CBS      -> CbsAdapter
 *   RTGS     -> RtgsAdapter
 *   SWIFT    -> SwiftAdapter
 *   NEFT/UPI -> NeftUpiAdapter
 *   FINTECH  -> FintechAdapter
 */
public enum SourceType {
    CBS,        // Core Banking System
    RTGS,       // Real-Time Gross Settlement
    SWIFT,      // Cross-border international transfers
    NEFT,       // National Electronic Funds Transfer
    UPI,        // Unified Payments Interface
    FINTECH,    // Third-party Fintech API
    INTERNAL    // Internal system transfers
}
