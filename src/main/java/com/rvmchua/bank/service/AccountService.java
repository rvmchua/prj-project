package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.util.AccountNumberMaker;

public class AccountService {
    public static void main(String[] args) {
//        Account account = null;
        AccountDAO accountDAO = new AccountDAOImpl();
//
//        account = getAccountByNumber("10000001");
//
//        System.out.println(account.toString());

        Account account = new Account();
        account.setFirstName("Royce");
        account.setLastName("Chua");
        account.setPinCode(2222);
        account.setAccountNumber("153982");

        updateAccount(account);
    }

    public static Account findAccountByNumber(String accountNumber) {
        AccountDAO accountDAO = new AccountDAOImpl();
        return accountDAO.getAccount(accountNumber);
    }

    public static void createNewAccount(Account account) {
        AccountDAO accountDAO = new AccountDAOImpl();
        int assignedAccNum = AccountNumberMaker.randomAccountNumber();

        do {
            accountDAO.doesAccountExists(String.valueOf(assignedAccNum));
            if (accountDAO.doesAccountExists(String.valueOf(assignedAccNum))) {
                assignedAccNum = AccountNumberMaker.randomAccountNumber();
            } else {
                account.setAccountNumber(String.valueOf(assignedAccNum));
            }
        } while (accountDAO.doesAccountExists(String.valueOf(assignedAccNum)));

        boolean result = accountDAO.createAccount(account);

        if (result) {
            System.out.println("Account created successfully");
        } else  {
            System.out.println("Account creation failed");
        }
    }

    public static void updateAccount(Account account) {
        AccountDAO accountDAO = new AccountDAOImpl();
        boolean result = accountDAO.updateAccountProfile(account);

        if (result) {
            System.out.println("Account updated successfully");
        } else  {
            System.out.println("Account update failed");
        }

    }

    public static void deleteAccount(String accountNumber) {
        AccountDAO accountDAO = new AccountDAOImpl();
        boolean result = accountDAO.deleteAccount(accountNumber);
    }
}
