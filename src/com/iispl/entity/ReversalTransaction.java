package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.iispl.enums.BankName;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class ReversalTransaction extends Transaction {

    private final String originalRef;

    public ReversalTransaction(UUID txnId, BigDecimal amount, 
    		TransactionType txnType, TransactionStatus txnStatus, 
    		BankName fromBank, BankName toBank, String channel, 
    		LocalDateTime txnDateTime, LocalDateTime valueDateTime,
    		String originalRef) 
    {
    	super(txnId, amount, txnType, txnStatus, fromBank, 
    			toBank, channel, valueDateTime, valueDateTime);
    	this.originalRef = originalRef;
    }

	public String getOriginalRef() {
		return originalRef;
	}
    
}