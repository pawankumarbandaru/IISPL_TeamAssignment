package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.iispl.enums.*;

public class DebitTransaction extends Transaction {

    public DebitTransaction(UUID txnId, BigDecimal amount, 
    		TransactionType txnType, TransactionStatus txnStatus, 
    		BankName fromBank, BankName toBank, String channel, 
    		LocalDateTime txnDateTime, LocalDateTime valueDateTime) 
    {
    	super(txnId, amount, txnType, txnStatus, fromBank, 
    			toBank, channel, valueDateTime, valueDateTime);
    }

	public DebitTransaction() {	}
}