package com.rvmchua.bank.dao;

import com.rvmchua.bank.model.Transaction;

import java.util.List;

public interface TransactionDAO {
    boolean saveTransaction(Transaction transaction);
    List<Transaction> getTransactionHistory(String accountNumber);
    Transaction getTransactionById(String transactionId);
    Transaction getLatestTransaction(String accountNumber);
}
