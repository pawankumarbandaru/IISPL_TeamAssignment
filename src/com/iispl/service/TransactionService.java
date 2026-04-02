package com.iispl.service;

import com.iispl.entity.IncomingTransaction;
import com.iispl.entity.Transaction;

public interface TransactionService {

    Transaction process(IncomingTransaction incoming);
}