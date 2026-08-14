package com.rvmchua.bank.dao.impl;

import com.rvmchua.bank.dao.TransactionDAO;
import com.rvmchua.bank.model.Transaction;
import com.rvmchua.bank.model.enums.TransactionType;
import com.rvmchua.bank.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public boolean saveTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (account_number, type, amount, balance_after) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setString(1, transaction.getAccountNumber());
            pStmt.setString(2, String.valueOf(transaction.getTransactionType()));
            pStmt.setBigDecimal(3, transaction.getAmount());
            pStmt.setBigDecimal(4, transaction.getBalanceAfter());

            int insertRow = pStmt.executeUpdate();
            return insertRow > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_number = ? LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setString(1, accountNumber);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getString("transaction_id"));
                transaction.setAccountNumber(rs.getString("account_number"));
                transaction.setTransactionType(TransactionType.valueOf(rs.getString("type")));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
                transaction.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        Transaction transaction = new Transaction();
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {

            pStmt.setString(1, transactionId);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                transaction.setTransactionId(rs.getString("transaction_id"));
                transaction.setAccountNumber(rs.getString("account_number"));
                transaction.setTransactionType(TransactionType.valueOf(rs.getString("type")));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
                transaction.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }
}
