package com.rvmchua.bank.menu;

import com.rvmchua.bank.service.AccountService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    public void start() {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printDisplay();
            System.out.print("Your choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1 -> AccountService.createNewAccount(input);
                    case 2 -> LoginMenu.start(input);
                    case 3 -> AccountService.showAllAccounts(input);
                    case 4 -> {
                        exit();
                        running = false;
                    }
                    default -> {
                        System.out.println("Invalid choice! Please try again.");
                        input.nextLine();
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                input.nextLine();
                input.nextLine();
            }
        }
    }

    private static void exit() {
        System.out.println("Thank you for choosing Banco de Yero!");
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static void printDisplay() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Welcome to the Banco de Yero!");
        System.out.println("Select your option:");
        System.out.println("1. Create an account");
        System.out.println("2. Login your account");
        System.out.println("3. List all accounts");
        System.out.println("4. Exit");
        System.out.println("---------------------------------------------------");
    }
}
