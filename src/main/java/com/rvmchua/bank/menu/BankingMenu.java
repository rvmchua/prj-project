package com.rvmchua.bank.menu;

import com.rvmchua.bank.service.AccountService;
import com.rvmchua.bank.service.TransactionService;
import lombok.NoArgsConstructor;

import java.util.Scanner;

public class BankingMenu {
    private AccountService accountService;
    private TransactionService transactionService;
    private Scanner input;


    public BankingMenu() {
        input = new Scanner(System.in);

    }

    public void start() {
        System.out.println("Welcome to Banking Menu");
        System.out.println("Please select your option: ");
        String option = input.nextLine();

        switch (option) {
            case "1": 
        }
    }
}
