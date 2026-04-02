package com.iispl.ingestion;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.iispl.entity.IncomingTransaction;
import com.iispl.register.AdapterRegistry;

/**
 * ════════════════════════════════════════════════════════════════════
 *  IngestionPipelineDemo  —  Entry Point
 * ════════════════════════════════════════════════════════════════════
 *
 * Demonstrates the complete ingestion flow:
 *
 *  Input files  (5 source systems)
 *       │
 *       ▼
 *  AdapterRegistry  ──detects SourceType──►  CbsAdapter
 *                                        ►  RtgsAdapter
 *                                        ►  SwiftAdapter
 *                                        ►  NeftUpiAdapter
 *                                        ►  FintechAdapter
 *       │
 *       ▼
 *  IncomingTransaction  (canonical POJO — same class for all sources)
 *       │
 *       ▼
 *  LinkedBlockingQueue<IncomingTransaction>
 *  (ready for SettlementProcessor — T3/T4's job)
 *
 * HOW TO RUN:
 *   javac -d out src/enums/*.java src/model/*.java src/adapter/*.java
 *               src/registry/*.java src/main/*.java
 *   java -cp out main.IngestionPipelineDemo
 *
 * Run from the bank-settlement/ folder so relative file paths resolve.
 */
public class IngestionPipelineDemo {

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  Bank Settlement — Ingestion Pipeline Demo            ");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ── 1. Build the registry (auto-registers all 5 adapters) ────────
        AdapterRegistry registry = new AdapterRegistry();
        System.out.println("\n" + registry + "\n");

        // ── 2. List of input files — one per source system ───────────────
        List<String> inputFiles = new ArrayList<>();
        inputFiles.add("DataSource/CBS_transactions.txt");
        inputFiles.add("DataSource/RTGS_transactions.txt");
        inputFiles.add("DataSource/SWIFT_transactions.json");
        inputFiles.add("DataSource/NEFT_UPI_transactions.csv");
        inputFiles.add("DataSource/FINTECH_transactions.json");

        // ── 3. Parse all files via the registry ──────────────────────────
        System.out.println("────────────────────────────────────────────────────────");
        System.out.println("  Parsing all source files...");
        System.out.println("────────────────────────────────────────────────────────\n");

        List<IncomingTransaction> all = registry.parseFiles(inputFiles);

        // ── 4. Assign sequential IDs ──────────────────────────────────────
        long idCounter = 1L;
        for (IncomingTransaction txn : all) {
            txn.setIncomingTxnId(idCounter++);
        }

        // ── 5. Place onto BlockingQueue (T4 IngestionWorker does this in threads) ─
        LinkedBlockingQueue<IncomingTransaction> queue = new LinkedBlockingQueue<>(1000);
        for (IncomingTransaction txn : all) {
            try {
                queue.put(txn);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while queuing transaction.");
            }
        }

        System.out.println("\n────────────────────────────────────────────────────────");
        System.out.println("  Queue loaded. Size = " + queue.size());
        System.out.println("────────────────────────────────────────────────────────\n");

        // ── 6. Print all canonical IncomingTransaction objects ────────────
        System.out.println("  Canonical IncomingTransaction objects:\n");
        for (IncomingTransaction txn : queue) {
            System.out.println(txn);
        }

        // ── 7. Summary ────────────────────────────────────────────────────
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.printf("  Total transactions ingested : %d%n", all.size());
        System.out.println("  All have status             : RECEIVED");
        System.out.println("  Next step                   : SettlementProcessor (T3/T4)");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
