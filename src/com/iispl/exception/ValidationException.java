package com.iispl.exception;

public class ValidationException extends TransactionProcessingException {

    public ValidationException(String message) {
        super(message);
    }
}