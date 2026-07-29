package com.banking.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import com.banking.dao.AccountDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.User;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtNationalID;
    private JTextField txtPhone;

    private JButton btnLogin;
    private JButton btnRegister;

    private UserDAO userDAO = new UserDAO();
    private AccountDAO accountDAO = new AccountDAO();

    // High-contrast color palette
    private final Color HEADER_BG = new Color(15, 23, 42); // Dark Slate
    private final Color FORM_BG = new Color(248, 250, 252); // Light Ice Blue

    // Login Button: Royal Blue -> Electric Blue (Hover)
    private final Color LOGIN_BASE = new Color(30, 64, 175);
    private final Color LOGIN_HOVER = new Color(59, 130, 246);

    // Register Button: Vibrant Emerald -> Bright Mint (Hover)
    private final Color REGISTER_BASE = new Color(4, 120, 87);
    private final Color REGISTER_HOVER = new Color(16, 185, 129);

    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Abeba Bank - Digital Banking Portal");
        setSize(480, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(FORM_BG);

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblTitle = new JLabel("ABEBA BANK", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Secure Online Banking Portal", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(148, 163, 184));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 12, 18));
        formPanel.setBackground(FORM_BG);
        formPanel.setBorder(new EmptyBorder(25, 35, 20, 35));

        formPanel.add(createStyledLabel("Full Name (Reg):"));
        txtFullName = createStyledTextField();
        formPanel.add(txtFullName);

        formPanel.add(createStyledLabel("National ID (Reg):"));
        txtNationalID = createStyledTextField();
        formPanel.add(txtNationalID);

        formPanel.add(createStyledLabel("Phone Number (Reg):"));
        txtPhone = createStyledTextField();
        formPanel.add(txtPhone);

        formPanel.add(createStyledLabel("Username:"));
        txtUsername = createStyledTextField();
        formPanel.add(txtUsername);

        formPanel.add(createStyledLabel("Password:"));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setBackground(FORM_BG);

        btnLogin = createHoverButton("Login", LOGIN_BASE, LOGIN_HOVER);
        btnRegister = createHoverButton("Register", REGISTER_BASE, REGISTER_HOVER);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> handleRegister());

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(30, 41, 59));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return tf;
    }

    private JButton createHoverButton(String text, Color baseColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(145, 42));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password.", "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.loginUser(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome to Abeba Bank, " + user.getFullName() + "!");
            new DashboardFrame(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String fullName = txtFullName.getText().trim();
        String nationalId = txtNationalID.getText().trim();
        String phone = txtPhone.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (fullName.isEmpty() || nationalId.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required to register.", "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches("^[0-9\\+\\-\\s]{9,15}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Phone Number (e.g., 0911223344).",
                    "Invalid Phone", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User(0, username, password, fullName, nationalId, phone);
        boolean userCreated = userDAO.registerUser(newUser);

        if (userCreated) {
            User createdUser = userDAO.loginUser(username, password);
            if (createdUser != null) {
                String accNumber = "ACC-" + (1000 + createdUser.getUserId());
                Account newAccount = new Account(0, createdUser.getUserId(), accNumber, BigDecimal.ZERO);
                accountDAO.createAccount(newAccount);

                JOptionPane.showMessageDialog(this,
                        "Welcome to Abeba Bank!\nRegistration Successful.\nYour Account Number: " + accNumber);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration Failed. Username, National ID, or Phone Number may already exist.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}