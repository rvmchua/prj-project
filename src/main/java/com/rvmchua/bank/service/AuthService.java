package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.model.Account;

import java.util.Optional;

public class AuthService {

    private final AccountDAO accountDAO;

    public AuthService() {
        this.accountDAO = new AccountDAOImpl();
    }

    public AuthService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Optional<Account> authenticate(String accountNumber, int enteredPin) {
        Account account = accountDAO.getAccountById(accountNumber);

        if (account == null) {
            return Optional.empty();
        }

        if (account.getPinCode() == enteredPin) {
            return Optional.of(account);
        } else {
            return Optional.empty();
        }
    }
}
