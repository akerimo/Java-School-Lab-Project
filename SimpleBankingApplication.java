

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

package lab.project;
import com.banking.dao.UserDAO;
import com.banking.model.User;



public class SimpleBankingApplication {
    public static void main(String[] args) {
    System.out.println("Attempt for creating the first user");
    
     
    User dummyUser = new User (0, "testuser2026", "securePass123", "Abdu Java");

    UserDAO userDAO = new UserDAO();

    boolean success = userDAO.registerUser(dummyUser);
    
    if (success) {
        System.out.println("Succes dummy user is registered");
    }

    else {
        System.out.println("Failed");
    }
    }
}