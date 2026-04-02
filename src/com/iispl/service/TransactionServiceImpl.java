package com.iispl.service;

import com.iispl.dao.TransactionDao;
import com.iispl.entity.*;
import com.iispl.factory.TransactionFactory;
import com.iispl.exception.*;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao dao = new TransactionDao();

    @Override
    public Transaction process(IncomingTransaction incoming) {

        validate(incoming);

        if (dao.exists(incoming.getSourceRef())) {
            throw new DuplicateTransactionException(incoming.getSourceRef());
        }

        enrich(incoming);

        Transaction txn = TransactionFactory.create(incoming);

        dao.save(txn.getReferenceNumber());

        return txn;
    }

    private void validate(IncomingTransaction in) {

        if (in == null) {
            throw new ValidationException("IncomingTransaction is null");
        }

        if (in.getSourceRef() == null) {
            throw new MissingFieldException("sourceRef");
        }

        if (in.getTxnType() == null) {
            throw new MissingFieldException("txnType");
        }

        if (in.getAmount() == null || in.getAmount().signum() <= 0) {
            throw new ValidationException("Invalid amount");
        }

        if (in.getCurrency() == null) {
            throw new MissingFieldException("currency");
        }

        if (in.getValueDate() == null) {
            throw new MissingFieldException("valueDate");
        }
    }

    private void enrich(IncomingTransaction in) {

        // Example enrichment
        if (in.getCurrency() == null) {
            in.setCurrency("INR");
        }
    }
}