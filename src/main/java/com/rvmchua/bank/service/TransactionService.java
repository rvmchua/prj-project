package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.TransactionDAO;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.model.Transaction;
import com.rvmchua.bank.model.enums.TransactionType;
import com.rvmchua.bank.model.enums.TransferType;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TransactionService {

    public static void withdraw(Account account, AccountDAO accountDAO, TransactionDAO transactionDAO, Scanner input) {
        BigDecimal newBalance;
        boolean accountResult = false;
        String accountNumber = account.getAccountNumber();
        Transaction latestTransaction;

        TransactionType type = TransactionType.WITHDRAW;

        while (!accountResult) {
            System.out.print("Enter amount to withdraw: Php ");
            BigDecimal amount = new BigDecimal(input.next());

            if (isSufficientFunds(account, amount)) {
                newBalance = account.getBalance().subtract(amount);
                accountResult = accountDAO.updateBalance(account.getAccountNumber(), newBalance);
                transactionDAO.saveTransaction(buildTransaction(account, type, amount, newBalance));
                latestTransaction = transactionDAO.getLatestTransaction(account.getAccountNumber());
                System.out.println("-----------------------------------------------------");
                System.out.println("Your transaction ID: " + latestTransaction.getTransactionId());
                System.out.println("You withdrawn: Php " + amount);
                System.out.println("Your balance: Php " + newBalance);
                System.out.println("-----------------------------------------------------");
                account = accountDAO.getAccountById(accountNumber);
            } else {
                System.out.println("Insufficient funds!");
            }
        }
        input.nextLine();
        promptEnterToContinue(input);
    }

    public static void deposit(Account account, AccountDAO accountDAO, TransactionDAO transactionDAO, Scanner input) {
        BigDecimal newBalance;
        boolean accountResult = false;
        boolean transactionResult;
        Transaction latestTransaction;
        String accountNumber = account.getAccountNumber();

        TransactionType type = TransactionType.DEPOSIT;

        while (!accountResult) {
            System.out.print("Enter amount to deposit: Php ");
            BigDecimal amount = new BigDecimal(input.next());
            newBalance = account.getBalance().add(amount);
            accountResult = accountDAO.updateBalance(account.getAccountNumber(), newBalance);
            transactionResult = transactionDAO.saveTransaction(buildTransaction(account, type, amount, newBalance));

            if (!accountResult || !transactionResult) {
                System.out.println("Unable to deposit funds! Try again later!");
                break;
            }
            account = accountDAO.getAccountById(accountNumber);
            latestTransaction = transactionDAO.getLatestTransaction(account.getAccountNumber());
            System.out.println("-----------------------------------------------------");
            System.out.println("Your transaction ID: " + latestTransaction.getTransactionId());
            System.out.println("You deposited: Php " + amount);
            System.out.println("Your balance: Php " + newBalance);
            System.out.println("-----------------------------------------------------");
        }
        input.nextLine();
        promptEnterToContinue(input);
    }

    public static void transfer(Account sender, AccountDAO accountDAO, TransactionDAO transactionDAO, Scanner input) {
        BigDecimal amount;
        Account receiver = null;
        String accountNumber = sender.getAccountNumber();

        TransactionType type = TransactionType.TRANSFER;

        while (receiver == null) {
            System.out.print("Enter account number of receiver (or 'cancel' to exit): ");
            String receivingAccountNumber = input.nextLine().trim();

            if ("cancel".equalsIgnoreCase(receivingAccountNumber)) {
                System.out.println("Transfer canceled.");
                return;
            }

            if (receivingAccountNumber.equals(sender.getAccountNumber())) {
                System.out.println("You cannot transfer money to your own account!");
                continue;
            }

            receiver = AccountService.findAccountByNumber(receivingAccountNumber, accountDAO);
            if (receiver == null) {
                System.out.println("Unable to find account by number! Please try again.");
            }
        }

        boolean transferSuccess = false;
        while (!transferSuccess) {
            System.out.print("Enter amount to transfer: Php ");
            String amountInput = input.nextLine().trim();

            try {
                amount = new BigDecimal(amountInput);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Transfer amount must be greater than zero.");
                    continue;
                }

                if (!isSufficientFunds(sender, amount)) {
                    System.out.println("Insufficient funds! Your current balance is: Php " + sender.getBalance());
                    continue;
                }

                BigDecimal newBalanceSender = sender.getBalance().subtract(amount);
                BigDecimal newBalanceReceiver = receiver.getBalance().add(amount);

                boolean isUpdateSenderSuccess = accountDAO.updateBalance(sender.getAccountNumber(), newBalanceSender);
                boolean isUpdateReceiverSuccess = accountDAO.updateBalance(receiver.getAccountNumber(), newBalanceReceiver);

                if (isUpdateSenderSuccess && isUpdateReceiverSuccess) {
                    sender.setBalance(newBalanceSender);
                    receiver.setBalance(newBalanceReceiver);

                    transactionDAO.saveTransaction(buildTransaction(sender, type, TransferType.SENDER, amount, newBalanceSender));
                    transactionDAO.saveTransaction(buildTransaction(receiver, type, TransferType.RECEIVER, amount, newBalanceReceiver));

                    transferSuccess = true;
                    sender = accountDAO.getAccountById(accountNumber);

                    Transaction latestTransaction = transactionDAO.getLatestTransaction(sender.getAccountNumber());
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Your transaction ID: " + latestTransaction.getTransactionId());
                    System.out.println("Successfully transferred " + amount + " from " + sender.getAccountNumber() + " to " + receiver.getAccountNumber());
                    System.out.println("Your current balance: Php " + sender.getBalance());
                    System.out.println("-----------------------------------------------------");
                } else {
                    System.out.println("Database update failed. Please try again later.");
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid amount entered! Please enter a valid number.");
            }
        }
        input.nextLine();
        promptEnterToContinue(input);
    }

    public static void findTransactionById(TransactionDAO transactionDAO, Scanner input) {
        boolean txnResult = false;

        while (!txnResult) {
            System.out.print("Please enter the transaction id: ");
            String txnId = input.nextLine();
            Transaction transaction = transactionDAO.getTransactionById(txnId);

            if (transaction != null) {
                System.out.println("Your transaction: " + transaction);
                txnResult = true;
            } else {
                System.out.println("Unable to find transaction with id: " + txnId);
            }
        }
        promptEnterToContinue(input);
    }

    public static void transactionHistory(String accountNumber, TransactionDAO transactionDAO, Scanner input) {
        List<Transaction> transactionHistory = transactionDAO.getTransactionHistory(accountNumber);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a");

        if (transactionHistory.isEmpty()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("No transactions found!");
            System.out.println("-----------------------------------------------------");
        } else {
            System.out.println("Your transactions:");
            int counter = 1;
            for (Transaction transaction : transactionHistory) {
                System.out.println("-----------------------------------------------------");
                System.out.println("Transaction number: " + counter);
                System.out.println("Transaction ID: " + transaction.getTransactionId());
                System.out.println("Transaction Type: " + transaction.getTransactionType());
                System.out.println("Amount: Php " + transaction.getAmount());
                System.out.println("Remaining balance: Php " + transaction.getBalanceAfter());
                System.out.println("Date of transaction: " + transaction.getCreatedAt().format(formatter));
                System.out.println("-----------------------------------------------------");
                counter++;
            }
        }
        promptEnterToContinue(input);
    }


//    Helper methods
    private static boolean isSufficientFunds(Account account, BigDecimal amount) {
        if (account == null || account.getBalance() == null) {
            return false;
        }
        return account.getBalance().compareTo(amount) >= 0;
    }

    private static Transaction buildTransaction (Account account, TransactionType type, BigDecimal amount, BigDecimal balance) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(account.getAccountNumber());
        transaction.setTransactionType(type);
        transaction.setBalanceAfter(balance);

        if (type == TransactionType.WITHDRAW) {
            transaction.setAmount(amount.negate());
        } else if (type == TransactionType.DEPOSIT) {
            transaction.setAmount(amount);
        }

        return transaction;
    }

    private static Transaction buildTransaction (Account account, TransactionType type, TransferType transferType, BigDecimal amount, BigDecimal balance) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(account.getAccountNumber());
        transaction.setTransactionType(type);
        transaction.setBalanceAfter(balance);

        if (transferType == TransferType.SENDER) {
            transaction.setAmount(amount.negate());
        } else {
            transaction.setAmount(amount);
        }

        return transaction;
    }

    private static void promptEnterToContinue(Scanner input) {
        System.out.println("Press ENTER to continue...");
        input.nextLine();
    }
}
