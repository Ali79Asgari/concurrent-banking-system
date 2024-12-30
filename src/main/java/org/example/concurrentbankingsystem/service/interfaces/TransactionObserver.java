package org.example.concurrentbankingsystem.service.interfaces;

import java.math.BigDecimal;

public interface TransactionObserver {

    void onTransaction(String sourceAccountNumber, String targetAccountNumber, String transactionType, BigDecimal amount);
}
