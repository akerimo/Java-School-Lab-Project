package com.banking.dao;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

public class AccountDAO {

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO Accounts (UserID, AccountNumber, Balance) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, account.getUserId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setBigDecimal(3, account.getBalance());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM Accounts WHERE UserID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getInt("AccountID"),
                            userId,
                            rs.getString("AccountNumber"),
                            rs.getBigDecimal("Balance"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account: " + e.getMessage());
        }
        return null;
    }

    // Handles Deposits and Withdrawals (Logs to Transactions Table)
    public boolean updateBalanceAndLog(int accountId, BigDecimal newBalance, String transactionType,
            BigDecimal amount) {
        String updateSql = "UPDATE Accounts SET Balance = ? WHERE AccountID = ?";
        String logSql = "INSERT INTO Transactions (AccountID, TransactionType, Amount) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Begin SQL transaction

            try (PreparedStatement pstmt1 = conn.prepareStatement(updateSql);
                    PreparedStatement pstmt2 = conn.prepareStatement(logSql)) {

                pstmt1.setBigDecimal(1, newBalance);
                pstmt1.setInt(2, accountId);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, accountId);
                pstmt2.setString(2, transactionType);
                pstmt2.setBigDecimal(3, amount);
                pstmt2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            return false;
        }
    }

    // Handles Inter-Account Transfers (Logs to Transfers Table)
    public boolean transferFunds(int senderUserId, String recipientAccNum, BigDecimal amount) {
        String recipientSql = "SELECT AccountID, Balance FROM Accounts WHERE AccountNumber = ?";
        String updateBalanceSql = "UPDATE Accounts SET Balance = ? WHERE AccountID = ?";
        String logTransferSql = "INSERT INTO Transfers (SendersAccountID, ReceiverAccountID, Amount) VALUES (?, ?, ?)";

        Account senderAccount = getAccountByUserId(senderUserId);
        if (senderAccount == null || senderAccount.getBalance().compareTo(amount) < 0) {
            return false; // Insufficient balance or invalid account
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int recipientAccountId = -1;
            BigDecimal recipientBalance = BigDecimal.ZERO;

            try (PreparedStatement pstmtRec = conn.prepareStatement(recipientSql)) {
                pstmtRec.setString(1, recipientAccNum);
                try (ResultSet rs = pstmtRec.executeQuery()) {
                    if (rs.next()) {
                        recipientAccountId = rs.getInt("AccountID");
                        recipientBalance = rs.getBigDecimal("Balance");
                    } else {
                        conn.rollback();
                        return false; // Recipient not found
                    }
                }
            }

            // Update sender balance
            try (PreparedStatement pstmtSender = conn.prepareStatement(updateBalanceSql)) {
                pstmtSender.setBigDecimal(1, senderAccount.getBalance().subtract(amount));
                pstmtSender.setInt(2, senderAccount.getAccountId());
                pstmtSender.executeUpdate();
            }

            // Update recipient balance
            try (PreparedStatement pstmtRec = conn.prepareStatement(updateBalanceSql)) {
                pstmtRec.setBigDecimal(1, recipientBalance.add(amount));
                pstmtRec.setInt(2, recipientAccountId);
                pstmtRec.executeUpdate();
            }

            // Record transfer record in Transfers table
            try (PreparedStatement pstmtTransfer = conn.prepareStatement(logTransferSql)) {
                pstmtTransfer.setInt(1, senderAccount.getAccountId());
                pstmtTransfer.setInt(2, recipientAccountId);
                pstmtTransfer.setBigDecimal(3, amount);
                pstmtTransfer.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Transfer failed: " + e.getMessage());
            return false;
        }
    }

    // Fetches Statement using exact column names: TransactionType and
    // TransactionDate
    public List<String> getStatement(int accountId) {
        List<String> statement = new ArrayList<>();
        String sql = "SELECT TransactionType, Amount, TransactionDate FROM Transactions WHERE AccountID = ? ORDER BY TransactionDate DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String record = rs.getTimestamp("TransactionDate") + " | "
                            + rs.getString("TransactionType") + " | $"
                            + rs.getBigDecimal("Amount");
                    statement.add(record);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving statement: " + e.getMessage());
        }
        return statement;
    }
}