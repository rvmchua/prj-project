package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.util.AccountNumberMaker;

import java.util.Scanner;

public class AccountService {

    public static void main(String[] args) {

    }

    public static Account findAccountByNumber(String accountNumber) {
        AccountDAO accountDAO = new AccountDAOImpl();
        return accountDAO.getAccountById(accountNumber);
    }

    public static void createNewAccount(Scanner input) {
        Account account = new Account();
        System.out.print("Enter your first name: ");
        account.setFirstName(input.nextLine());
        System.out.print("Enter your last name: ");
        account.setLastName(input.nextLine());
        System.out.print("Enter your 4-6 digit PIN: ");
        account.setPinCode(input.nextInt());
        input.nextLine();

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
            System.out.println("Your account number: " + account.getAccountNumber());
        } else  {
            System.out.println("Account creation failed");
        }

        System.out.println("Returning to main menu...");
        return;
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
        Scanner input = new Scanner(System.in);
        System.out.print("Are you sure you want to delete your account (y/N)? ");
        String confirmation = input.nextLine().trim();

        if (confirmation.equalsIgnoreCase("y")) {
            AccountDAO accountDAO = new AccountDAOImpl();

            boolean isDeleted = accountDAO.deleteAccount(accountNumber);

            if (isDeleted) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete account.");
            }
        } else {
            System.out.println("Account deletion cancelled.");
        }
    }

    public static void balanceInquiry(String accountNumber) {
        AccountDAO accountDAO = new AccountDAOImpl();
        Account account = accountDAO.getAccountById(accountNumber);

        System.out.println("Account balance: " + account.getBalance());
    }

    private static void printHeader(){
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to the Banco de Yero!");
    }

    private static void printFooter(){
        System.out.println("----------------------------------------------------");
    }
}
