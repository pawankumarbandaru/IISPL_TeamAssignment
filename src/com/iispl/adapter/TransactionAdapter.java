package com.iispl.adapter;


import java.util.List;

import com.iispl.entity.IncomingTransaction;
import com.iispl.enums.SourceType;

/**
 * ════════════════════════════════════════════════════════════════════
 *  TransactionAdapter  —  Strategy Interface
 * ════════════════════════════════════════════════════════════════════
 *
 * Every source-system adapter (CBS, RTGS, SWIFT, NEFT/UPI, Fintech)
 * implements this interface.
 *
 * Contract:
 *   adapt(rawPayload)  – convert ONE raw string record → IncomingTransaction
 *   adaptFile(path)    – parse an ENTIRE file → List<IncomingTransaction>
 *   getSourceType()    – identify which source system this adapter handles
 *
 * The AdapterRegistry uses getSourceType() as the lookup key so the
 * correct adapter is chosen at runtime without any if/switch in the caller.
 *
 * Design pattern: Strategy
 */
public interface TransactionAdapter {

    /**
     * Converts a single raw string record into a canonical IncomingTransaction.
     *
     * @param rawPayload  One record string (a TXT line, a JSON object,
     *                    or a CSV row — depends on the adapter).
     * @return            Fully populated IncomingTransaction with
     *                    status = RECEIVED.
     * @throws IllegalArgumentException if the payload is null or malformed.
     */
    IncomingTransaction adapt(String rawPayload);

    /**
     * Opens the given file, iterates every record, and adapts each one.
     *
     * @param filePath  Path to the source file.
     * @return          List of IncomingTransaction; never null, may be empty.
     */
    List<IncomingTransaction> adaptFile(String filePath);

    /**
     * Returns the SourceType that this adapter handles.
     * Used by AdapterRegistry as the map key.
     */
    SourceType getSourceType();
}