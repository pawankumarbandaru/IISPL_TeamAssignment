package com.iispl.exception;

public class DuplicateTransactionException extends TransactionProcessingException {

    public DuplicateTransactionException(String ref) {
        super("Duplicate transaction for reference: " + ref);
    }
}
