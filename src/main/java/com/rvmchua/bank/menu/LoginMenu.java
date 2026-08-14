package com.rvmchua.bank.menu;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.service.AuthService;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class LoginMenu {

    public static void start (Scanner input) {
        AccountDAO accountDAO = new AccountDAOImpl();
        AuthService authService = new AuthService(accountDAO);

        printHeader();
        System.out.println("Welcome to the Login Menu!");

        System.out.print("Please enter your account number: ");
        String accountNumber = input.nextLine();

        System.out.print("Please enter your PIN code: ");
        int pinCode;

        try {
            pinCode = input.nextInt();
            input.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid PIN format! PIN must contain numbers only.");
            input.nextLine();
            promptEnterToContinue(input);
            return;
        }

        Optional<Account> authResult = authService.authenticate(accountNumber, pinCode);

        if (authResult.isPresent()) {
            Account account = authResult.get();
            System.out.println("Successfully logged in! Welcome, " +  account.getFirstName() + " " + account.getLastName());
            promptEnterToContinue(input);
            AccountDashboard.menu(input, account);

        } else  {
            System.out.println("Invalid account number or PIN code!");
            promptEnterToContinue(input);
        }

    }

    private static void promptEnterToContinue(Scanner input) {
        System.out.println("Press ENTER to continue...");
        input.nextLine();
    }

    private static void printHeader() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to the Banco de Yero!");
    }

}
