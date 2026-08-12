package com.rvmchua.bank.dao;

import com.rvmchua.bank.model.Account;

public interface AccountDAO {
    Account getAccount(String accountId);
    boolean createAccount(Account account);
    boolean updateAccount(Account account);
    void deleteAccount(String accountId);
    boolean doesAccountExists(String accountId);
}
