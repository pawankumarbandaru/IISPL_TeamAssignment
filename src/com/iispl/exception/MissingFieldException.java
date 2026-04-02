package com.iispl.exception;

public class MissingFieldException extends TransactionProcessingException {

    public MissingFieldException(String field) {
        super(field + " is missing");
    }
}