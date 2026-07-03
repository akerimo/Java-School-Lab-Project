package com.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.banking.model.User;
import com.banking.util.DatabaseConnection;

public class UserDAO {
    
    public boolean registerUser(User user){
        String sql = "Insert INTO Users (Username, Password, FullName) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                //Binding parameters securely to prevent SQL Injection
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());

            int rowAffected= pstmt.executeUpdate();
            return rowAffected > 0; // returns true if the user was successfully inserted
            }
            catch (SQLException e){
                System.err.println("Error during user registration: " + e.getMessage());
                return false;
            }
    }
}
