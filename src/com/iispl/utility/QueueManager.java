package com.iispl.utility;

import com.iispl.entity.Transaction;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager {

    public static final BlockingQueue<Transaction> creditQueue   = new LinkedBlockingQueue<>();
    public static final BlockingQueue<Transaction> debitQueue    = new LinkedBlockingQueue<>();
    public static final BlockingQueue<Transaction> reversalQueue = new LinkedBlockingQueue<>();
    public static final BlockingQueue<Transaction> interbank    = new LinkedBlockingQueue<>();

}