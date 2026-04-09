package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.iispl.enums.BankName;
import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class CreditTransaction extends Transaction {

	public CreditTransaction(UUID txnId, BigDecimal amount, 
    		TransactionType txnType, TransactionStatus txnStatus, 
    		BankName fromBank, BankName toBank, String channel, 
    		LocalDateTime txnDateTime, LocalDateTime valueDateTime) 
    {
    	super(txnId, amount, txnType, txnStatus, fromBank, 
    			toBank, channel, valueDateTime, valueDateTime);
    }

	public CreditTransaction() {	}
}