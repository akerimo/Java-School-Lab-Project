
/*
This is Java lab project, Which implements OOP using Java. with GUI and Exception handling. Also Database have a Database connection
Project Title: Simple Banking Application
              1. user can create account
              2. user can make deposit
              3. user can make WithDrawal
              4. user can transfer funds
              5. user can see their balance
              6. user can see their full statements
              7. user can make currency converter
*/

//Github repo: https://github.com/akerimo/Java-School-Lab-Project

import javax.swing.SwingUtilities;
import com.banking.ui.LoginFrame;;

public class SimpleBankingApplication {
    public static void main(String[] args) {
        // Launch Swing GUI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}