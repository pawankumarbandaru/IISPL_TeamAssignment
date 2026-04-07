package com.iispl.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BatchRecord — one record per transaction, created after batch is sealed.
 *
 * Fields: recordId, batchId, txnId, amount, status, date,
 *         channel, txnType, fromBank, toBank, valueDate
 */
public class BatchRecord {

    public enum RecordStatus { PENDING, SETTLED, REJECTED }

    private final String             recordId;
    private final String             batchId;
    private final String             txnId;
    private final double             amount;
    private RecordStatus             status;
    private final LocalDateTime      date;
    private final Transaction.Channel  channel;
    private final Transaction.TxnType  txnType;
    private final String             fromBank;
    private final String             toBank;
    private final LocalDate          valueDate;

    public BatchRecord(String batchId, Transaction txn) {
        this.recordId  = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.batchId   = batchId;
        this.txnId     = txn.getTxnId();
        this.amount    = txn.getAmount();
        this.status    = RecordStatus.PENDING;
        this.date      = LocalDateTime.now();
        this.channel   = txn.getChannel();
        this.txnType   = txn.getTxnType();
        this.fromBank  = txn.getFromBank();
        this.toBank    = txn.getToBank();
        this.valueDate = txn.getValueDate();
    }

    public String               getRecordId()  { return recordId; }
    public String               getBatchId()   { return batchId; }
    public String               getTxnId()     { return txnId; }
    public double               getAmount()    { return amount; }
    public RecordStatus         getStatus()    { return status; }
    public LocalDateTime        getDate()      { return date; }
    public Transaction.Channel  getChannel()   { return channel; }
    public Transaction.TxnType  getTxnType()   { return txnType; }
    public String               getFromBank()  { return fromBank; }
    public String               getToBank()    { return toBank; }
    public LocalDate            getValueDate() { return valueDate; }
    public void                 setStatus(RecordStatus s) { this.status = s; }
}