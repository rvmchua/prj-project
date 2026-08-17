package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.util.AccountNumberMaker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AccountService {

    public static Account findAccountByNumber(String accountNumber, AccountDAO accountDAO) {
        return accountDAO.getAccountById(accountNumber);
    }

    public static void createNewAccount(Scanner input) {
        Account account = new Account();
        AccountDAO accountDAO = new AccountDAOImpl();
        BigDecimal initialDeposit = BigDecimal.ZERO;
        boolean validDeposit = false;

        printHeader();

        System.out.print("Enter your first name: ");
        account.setFirstName(input.nextLine().trim());

        System.out.print("Enter your last name: ");
        account.setLastName(input.nextLine().trim());

        System.out.print("Enter your 4-6 digit PIN: ");
        String pin = input.nextLine().trim();
        account.setPinCode(Integer.parseInt(pin));

        while (!validDeposit) {
            System.out.print("Enter initial deposit amount: Php ");
            String depositInput = input.nextLine().trim();

            try {
                initialDeposit = new BigDecimal(depositInput);
                if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Invalid initial deposit amount. Enter an amount greater than or equal to 0.");
                } else {
                    validDeposit = true;
                    account.setBalance(initialDeposit);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please try again.");
            }
        }

        String assignedAccNum;
        do {
            assignedAccNum = String.valueOf(AccountNumberMaker.randomAccountNumber());
        } while (accountDAO.doesAccountExists(assignedAccNum));

        account.setAccountNumber(assignedAccNum);

        boolean result = accountDAO.createAccount(account);

        if (result) {
            printHeader();
            System.out.println("Account created successfully!");
            System.out.println("----------------------------------------------------");
            System.out.println("Account number: " + account.getAccountNumber());
            System.out.println("Account name: " + account.getFirstName() + " " + account.getLastName());
            System.out.println("Account balance: Php " + account.getBalance());
            System.out.println("----------------------------------------------------");

            if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
                TransactionService.initialDeposit(account);
            }
        } else {
            System.out.println("Account creation failed. Please try again.");
        }

        System.out.println("Returning to main menu...");
        promptEnterToContinue(input);
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

    public static void balanceInquiry(Account account, Scanner input) {
        System.out.println("Your account balance is: Php " + account.getBalance());
        System.out.println("----------------------------------------------------");
        promptEnterToContinue(input);
    }

    public static void showAllAccounts(Scanner input) {
        AccountDAO accountDAO = new AccountDAOImpl();
        List<Account> allAccounts = accountDAO.getAllAccounts();

        if (allAccounts.isEmpty()) {
            System.out.println("----------------------------------------------------");
            System.out.println("No accounts found");
            System.out.println("----------------------------------------------------");
        } else {
            System.out.println("----------------------------------------------------");
            System.out.println("Accounts available");
            System.out.println("----------------------------------------------------");
            for (Account account : allAccounts) {
                System.out.println("Account number: " + account.getAccountNumber());
                System.out.println("Account First Name: " + account.getFirstName());
                System.out.println("Account Last Name: " + account.getLastName());
                System.out.println("Available Balance: Php " + account.getBalance());
                System.out.println("----------------------------------------------------");
            }
        }
        promptEnterToContinue(input);
    }

    private static void printHeader(){
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to the Banco de Yero!");
        System.out.println("----------------------------------------------------");
    }

    private static void promptEnterToContinue(Scanner input) {
        System.out.println("Press ENTER to continue...");
        input.nextLine();
    }

}
