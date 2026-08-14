package com.rvmchua.bank.menu;

import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.service.AccountService;
import com.rvmchua.bank.service.TransactionService;


import java.util.Scanner;

public class AccountDashboard {
    private AccountService accountService;
    private TransactionService transactionService;

    public static void menu(Scanner input, Account account) {

        System.out.println("Please select your option: ");
        String option = input.nextLine();

        System.out.println("You input: " + option);
        System.exit(0);

    }

    private static void printHeader() {
        System.out.println("-----------------------------------------------------");
    }
}
