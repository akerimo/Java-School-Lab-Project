package com.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.banking.model.User;
import com.banking.util.DatabaseConnection;

public class UserDAO {
    
    public boolean registerUser(User user){
        String sql = "Insert INTO Users (Username, Password, FullName) VALUES (?, ?, ?)";
    }
}
