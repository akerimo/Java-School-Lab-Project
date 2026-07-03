package lab.project;

/*
This is Java lab project, Which implements OOP using Java. with GUI and Exception handling. Also Database
Project Title: Simple Banking Application
              1. user can create account
              2. user can make deposit
              3. user can make WithDrawal
              4. user can transfer funds
              5. user can see their balance
              6. user can see their full statements
              7. user can make currency converter
*/


import java.sql.Connection;
import java.sql.SQLException;
import com.banking.util.DatabaseConnection;

public class SimpleBankingApplication {
    public static void main(String[] args) {
        System.out.println("Attempting to connect to MS SQL Server...");

        // Try-with-resources automatically closes the connection when done
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCCESS: Connected to the database smoothly!");
            }
        } catch (SQLException e) {
            System.err.println("CONNECTION FAILED!");
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}