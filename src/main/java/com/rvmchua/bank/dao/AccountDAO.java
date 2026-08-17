package com.rvmchua.bank.dao;

import com.rvmchua.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    Account getAccountById(String accountId);
    boolean createAccount(Account account);
    boolean updateAccountProfile(Account account);
    boolean deleteAccount(String accountId);
    boolean doesAccountExists(String accountId);
    boolean updateBalance(String accountNumber, BigDecimal balance);
    List<Account> getAllAccounts();
}
