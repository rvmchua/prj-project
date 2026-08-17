package com.rvmchua.bank.menu;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.TransactionDAO;
import com.rvmchua.bank.dao.impl.TransactionDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.service.AccountService;
import com.rvmchua.bank.service.TransactionService;


import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountDashboard {

    public static void menu(Scanner input, Account account, AccountDAO accountDAO) {
        TransactionDAO transactionDAO = new TransactionDAOImpl();
        boolean isMenuRunning = true;

        while (isMenuRunning) {
            try {
                printMenu();
                System.out.print("Please select your option: ");
                String option = input.nextLine();

                switch (option) {
                    case "1" -> AccountService.balanceInquiry(account, input);
                    case "2" -> TransactionService.transactionHistory(account.getAccountNumber(), transactionDAO, input);
                    case "3" -> TransactionService.withdraw(account, accountDAO, transactionDAO, input);
                    case "4" -> TransactionService.deposit(account, accountDAO, transactionDAO, input);
                    case "5" -> TransactionService.transfer(account, accountDAO, transactionDAO, input);
                    case "6" -> TransactionService.findTransactionById(transactionDAO, input);
                    case "7" -> {
                        exit();
                        isMenuRunning = false;
                    }
                    default -> {
                        System.out.println("Invalid choice! Please try again.");
                        input.nextLine();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number");
                input.nextLine();
                input.nextLine();
            }
        }
    }

    private static void printMenu() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to the Banco de Yero!");
        System.out.println("-----------------------------------------------------");
        System.out.println("1. Balance Inquiry");
        System.out.println("2. Transaction History");
        System.out.println("3. Withdraw");
        System.out.println("4. Deposit");
        System.out.println("5. Transfer Funds");
        System.out.println("6. Find Transaction by ID");
        System.out.println("7. Exit");
        System.out.println("-----------------------------------------------------");

    }

    private static void exit() {
        System.out.println("Thank you for choosing Banco de Yero!");
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
