package com.rvmchua.bank.service;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.dao.TransactionDAO;
import com.rvmchua.bank.dao.impl.AccountDAOImpl;
import com.rvmchua.bank.dao.impl.TransactionDAOImpl;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.model.Transaction;
import com.rvmchua.bank.model.enums.TransactionType;
import com.rvmchua.bank.model.enums.TransferType;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TransactionService {
    public static void main(String[] args) {

//        deposit("1000001");
//        withdraw("1344391");
//        getTransactionById("1000001");
//        transfer("1000001","1344391");
//        transactionHistory("1378886");
    }

    public static void withdraw(String accountNumber) {
        Scanner input = new Scanner(System.in);
        BigDecimal newBalance;
        boolean accountResult = false;
        boolean transactionResult = false;

        TransactionType type = TransactionType.WITHDRAW;
        Account account = AccountService.findAccountByNumber(accountNumber);
        AccountDAO accountDAO = new AccountDAOImpl();
        TransactionDAO transactionDAO = new TransactionDAOImpl();

        while (!accountResult) {
            System.out.print("Enter amount to withdraw: ");
            BigDecimal amount = new BigDecimal(input.next());

            if (isSufficientFunds(account, amount)) {
                newBalance = account.getBalance().subtract(amount);
                accountResult = accountDAO.updateBalance(accountNumber, newBalance);
                transactionResult = transactionDAO.saveTransaction(buildTransaction(account, type, amount, newBalance));
                System.out.println("You withdrawn: " + amount.toString());
                System.out.println("Your balance: " + newBalance.toString());
            } else {
                System.out.println("Insufficient funds!");
            }
        }
        input.close();

    }

    public static void deposit(String accountNumber) {
        Scanner input = new Scanner(System.in);
        BigDecimal newBalance;
        boolean accountResult = false;
        boolean transactionResult = false;

        TransactionType type = TransactionType.DEPOSIT;
        Account account = AccountService.findAccountByNumber(accountNumber);
        AccountDAO accountDAO = new AccountDAOImpl();
        TransactionDAO transactionDAO = new TransactionDAOImpl();

        while (!accountResult) {
            System.out.print("Enter amount to deposit: ");
            BigDecimal amount = new BigDecimal(input.next());
            newBalance = account.getBalance().add(amount);
            accountResult = accountDAO.updateBalance(accountNumber, newBalance);
            transactionResult = transactionDAO.saveTransaction(buildTransaction(account, type, amount, newBalance));

            if (!accountResult || !transactionResult) {
                System.out.println("Unable to deposit funds! Try again later!");
                break;
            }

            System.out.println("You deposited: " + amount.toString());
            System.out.println("Your balance: " + newBalance.toString());
        }
        input.close();
    }

    public static void transfer(String fromAccountNumber, String toAccountNumber) {
        Scanner input = new Scanner(System.in);
        BigDecimal amount;
        boolean accountResult = false;

        TransactionType type = TransactionType.TRANSFER;
        Account sender =  AccountService.findAccountByNumber(fromAccountNumber);
        Account receiver =  AccountService.findAccountByNumber(toAccountNumber);
        AccountDAO accountDAO = new AccountDAOImpl();
        TransactionDAO transactionDAO = new TransactionDAOImpl();

        while (!accountResult) {
            System.out.print("Enter amount to transfer: ");
            amount = new BigDecimal(input.next());

            if (isSufficientFunds(sender, amount)) {
                BigDecimal newBalanceSender;
                BigDecimal newBalanceReceiver;

                newBalanceSender = sender.getBalance().subtract(amount);
                newBalanceReceiver = receiver.getBalance().add(amount);

                boolean isUpdateSenderSuccess = accountDAO.updateBalance(fromAccountNumber, newBalanceSender);
                boolean isUpdateReceiverSuccess = accountDAO.updateBalance(toAccountNumber, newBalanceReceiver);
                if (isUpdateSenderSuccess && isUpdateReceiverSuccess) {
                    accountResult = true;
                    boolean isTxnSenderSuccess = transactionDAO.saveTransaction(buildTransaction(sender, type, TransferType.SENDER, amount, newBalanceSender));
                    boolean isTxnReceiverSuccess = transactionDAO.saveTransaction(buildTransaction(receiver, type, TransferType.RECEIVER, amount, newBalanceReceiver));
                    System.out.println("Successfully transferred " + amount +  " from " + fromAccountNumber + " to " + toAccountNumber);
                } else {
                    System.out.println("Insufficient funds!");
                }
            }
        }

    }

    public static void findTransactionById(String accountNumber) {
        Scanner input = new Scanner(System.in);
        TransactionDAO transactionDAO = new TransactionDAOImpl();
        boolean txnResult = false;
        Account account = AccountService.findAccountByNumber(accountNumber);

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
    }

    public static void transactionHistory(String accountNumber) {
        TransactionDAO transactionDAO = new TransactionDAOImpl();
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
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("Remaining balance: " + transaction.getBalanceAfter());
                System.out.println("Date of transaction: " + transaction.getCreatedAt().format(formatter));
                System.out.println("-----------------------------------------------------");
                counter++;
            }
        }
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
}
