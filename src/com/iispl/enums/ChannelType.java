package com.iispl.enums;



/**
 * Payment rail / channel through which a transaction is routed.
 */
public enum ChannelType {
    RTGS,       // Real-Time Gross Settlement  (high-value, same-day)
    NEFT,       // National Electronic Funds Transfer (batch, hourly)
    UPI,        // Unified Payments Interface  (instant retail)
    SWIFT,      // Society for Worldwide Interbank Financial Telecommunication
    ACH,        // Automated Clearing House
    INTERNAL    // Intra-bank book transfer — no external payment rail used
}
