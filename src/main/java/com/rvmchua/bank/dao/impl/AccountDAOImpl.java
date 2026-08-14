package com.rvmchua.bank.dao.impl;

import com.rvmchua.bank.dao.AccountDAO;
import com.rvmchua.bank.model.Account;
import com.rvmchua.bank.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public Account getAccountById(String accountId) {
        Account account = new Account();
        String query = "SELECT * FROM accounts where account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                account.setAccountNumber(rs.getString("account_number"));
                account.setFirstName(rs.getString("first_name"));
                account.setLastName(rs.getString("last_name"));
                account.setPinCode(rs.getInt("pin_code"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                account.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            }
            rs.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public boolean createAccount(Account account) {
        String query = "INSERT INTO accounts (account_number, first_name, last_name, pin_code) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {

            pStmt.setString(1,account.getAccountNumber());
            pStmt.setString(2,account.getFirstName());
            pStmt.setString(3,account.getLastName());
            pStmt.setInt(4,account.getPinCode());

            int createdRow = pStmt.executeUpdate();

            return (createdRow > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean doesAccountExists(String accountNumber) {
        String query = "SELECT EXISTS(SELECT 1 FROM accounts WHERE account_number = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setString(1,accountNumber);

            try (ResultSet rs = pStmt.executeQuery();) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAccountProfile(Account account) {
        String query = "UPDATE accounts SET first_name = ?, last_name = ?, pin_code = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setString(1, account.getFirstName());
            pStmt.setString(2, account.getLastName());
            pStmt.setInt(3, account.getPinCode());
            pStmt.setString(4, account.getAccountNumber());

            int updatedRow = pStmt.executeUpdate();
            return updatedRow > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBalance(String accountNumber, BigDecimal balance) {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setBigDecimal(1, balance);
            pStmt.setString(2, accountNumber);

            int updatedRow = pStmt.executeUpdate();

            return updatedRow > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAccount(String accountNumber) {
        String query =  "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pStmt = conn.prepareStatement(query);) {
            pStmt.setString(1, accountNumber);

            int deletedRows = pStmt.executeUpdate();
            return deletedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
