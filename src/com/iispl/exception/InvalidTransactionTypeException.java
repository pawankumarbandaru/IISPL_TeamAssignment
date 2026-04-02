package com.iispl.exception;

public class InvalidTransactionTypeException extends TransactionProcessingException {

    public InvalidTransactionTypeException(String type) {
        super("Invalid transaction type: " + type);
    }
}