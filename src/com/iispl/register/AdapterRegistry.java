package com.iispl.register;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.iispl.adapter.CbsAdapter;
import com.iispl.adapter.FintechAdapter;
import com.iispl.adapter.NeftUpiAdapter;
import com.iispl.adapter.RtgsAdapter;
import com.iispl.adapter.SwiftAdapter;
import com.iispl.adapter.TransactionAdapter;
import com.iispl.entity.IncomingTransaction;
import com.iispl.enums.SourceType;

/**
 * ════════════════════════════════════════════════════════════════════
 *  AdapterRegistry
 * ════════════════════════════════════════════════════════════════════
 *
 * Central registry that maps each SourceType to its TransactionAdapter.
 * The architecture document shows this as:
 *
 *   Map<SourceType, TransactionAdapter>
 *
 * At runtime, the caller passes the file path. The registry:
 *   1. Detects the SourceType from the filename prefix (CBS_, RTGS_, etc.)
 *   2. Looks up the correct adapter from the map
 *   3. Delegates the parse to that adapter
 *
 * To add a new source: write a new Adapter class, add one line in
 * registerDefaults() — no other code changes needed.
 *
 * Collections used:
 *   EnumMap<SourceType, TransactionAdapter>
 *     — most efficient Map when keys are an enum (O(1), compact)
 */
public class AdapterRegistry {

    /** Internal store: SourceType → adapter implementation. */
    private final Map<SourceType, TransactionAdapter> registry;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public AdapterRegistry() {
        registry = new EnumMap<>(SourceType.class);
        registerDefaults();
    }

    // ----------------------------------------------------------------
    // Registration
    // ----------------------------------------------------------------

    /** Called once at startup to wire all built-in adapters. */
    private void registerDefaults() {
        register(SourceType.CBS,     new CbsAdapter());
        register(SourceType.RTGS,    new RtgsAdapter());
        register(SourceType.SWIFT,   new SwiftAdapter());
        register(SourceType.NEFT,    new NeftUpiAdapter());   // handles both NEFT & UPI
        register(SourceType.UPI,     new NeftUpiAdapter());   // same adapter, two keys
        register(SourceType.FINTECH, new FintechAdapter());
    }

    /**
     * Registers (or replaces) the adapter for a given SourceType.
     * Public so tests can inject mock adapters.
     */
    public void register(SourceType sourceType, TransactionAdapter adapter) {
        if (sourceType == null || adapter == null) {
            throw new IllegalArgumentException(
                "AdapterRegistry: sourceType and adapter must not be null.");
        }
        registry.put(sourceType, adapter);
        System.out.println("[AdapterRegistry] Registered: "
            + sourceType + " -> " + adapter.getClass().getSimpleName());
    }

    // ----------------------------------------------------------------
    // Lookup and delegation
    // ----------------------------------------------------------------

    /**
     * Returns the adapter for the given SourceType.
     *
     * @throws IllegalArgumentException if no adapter is registered.
     */
    public TransactionAdapter getAdapter(SourceType sourceType) {
        TransactionAdapter adapter = registry.get(sourceType);
        if (adapter == null) {
            throw new IllegalArgumentException(
                "AdapterRegistry: no adapter registered for: " + sourceType);
        }
        return adapter;
    }

    /**
     * Convenience: detect SourceType from filename, then parse the file.
     *
     * Filename convention:
     *   CBS_transactions.txt     → SourceType.CBS    → CbsAdapter
     *   RTGS_transactions.txt    → SourceType.RTGS   → RtgsAdapter
     *   SWIFT_transactions.json  → SourceType.SWIFT  → SwiftAdapter
     *   NEFT_transactions.csv    → SourceType.NEFT   → NeftUpiAdapter
     *   FINTECH_transactions.json→ SourceType.FINTECH→ FintechAdapter
     *
     * @param filePath Path to the input file.
     * @return         List of IncomingTransaction objects.
     */
    public List<IncomingTransaction> parseFile(String filePath) {
        SourceType sourceType = detectSourceType(filePath);
        TransactionAdapter adapter = getAdapter(sourceType);
        System.out.println("[AdapterRegistry] Routing '"
            + filePath + "' -> " + adapter.getClass().getSimpleName());
        return adapter.adaptFile(filePath);
    }

    /**
     * Parses multiple files and returns all transactions in one flat list.
     */
    public List<IncomingTransaction> parseFiles(List<String> filePaths) {
        List<IncomingTransaction> all = new ArrayList<>();
        for (String path : filePaths) {
            all.addAll(parseFile(path));
        }
        System.out.println("[AdapterRegistry] Total ingested: " + all.size());
        return all;
    }

    // ----------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------

    /**
     * Detects SourceType from the filename prefix (case-insensitive).
     * Convention: the file name starts with the source system name,
     * e.g. "CBS_transactions.txt", "RTGS_20240301.txt".
     */
    public static SourceType detectSourceType(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException(
                "AdapterRegistry: filePath is null/empty.");
        }
        // Extract just the filename (strip directory path)
        String fileName = filePath.contains("/")
            ? filePath.substring(filePath.lastIndexOf('/') + 1)
            : filePath.contains("\\")
            ? filePath.substring(filePath.lastIndexOf('\\') + 1)
            : filePath;

        String upper = fileName.toUpperCase();

        if      (upper.startsWith("CBS"))     return SourceType.CBS;
        else if (upper.startsWith("RTGS"))    return SourceType.RTGS;
        else if (upper.startsWith("SWIFT"))   return SourceType.SWIFT;
        else if (upper.startsWith("NEFT"))    return SourceType.NEFT;
        else if (upper.startsWith("UPI"))     return SourceType.UPI;
        else if (upper.startsWith("FINTECH")) return SourceType.FINTECH;
        else {
            throw new IllegalArgumentException(
                "AdapterRegistry: cannot detect SourceType from filename: "
                + fileName
                + ". Expected filename to start with: CBS / RTGS / SWIFT / NEFT / UPI / FINTECH");
        }
    }

    /** Diagnostic — shows all currently registered source types. */
    public List<SourceType> registeredSources() {
        return new ArrayList<>(registry.keySet());
    }

    @Override
    public String toString() {
        return "AdapterRegistry { registered=" + registry.keySet() + " }";
    }
}
