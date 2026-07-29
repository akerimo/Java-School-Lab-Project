package com.banking.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import com.banking.model.User;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private Account currentAccount;
    private AccountDAO accountDAO = new AccountDAO();

    private JLabel lblWelcome;
    private JLabel lblDetails;
    private JLabel lblBalance;
    private JTextField txtAmount;

    // High-contrast color palette
    private final Color HEADER_BG = new Color(15, 23, 42);
    private final Color FORM_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;

    // Button Colors (Normal -> Hover)
    private final Color DEPOSIT_BASE = new Color(30, 64, 175);
    private final Color DEPOSIT_HOVER = new Color(59, 130, 246);

    private final Color WITHDRAW_BASE = new Color(180, 83, 9);
    private final Color WITHDRAW_HOVER = new Color(245, 158, 11);

    private final Color TRANSFER_BASE = new Color(109, 40, 217);
    private final Color TRANSFER_HOVER = new Color(139, 92, 246);

    private final Color STATEMENT_BASE = new Color(13, 148, 136);
    private final Color STATEMENT_HOVER = new Color(20, 184, 166);

    private final Color CONVERTER_BASE = new Color(4, 120, 87);
    private final Color CONVERTER_HOVER = new Color(16, 185, 129);

    private final Color LOGOUT_BASE = new Color(185, 28, 28);
    private final Color LOGOUT_HOVER = new Color(239, 68, 68);

    public DashboardFrame(User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        this.currentUser = user;
        this.currentAccount = accountDAO.getAccountByUserId(user.getUserId());

        setTitle("Abeba Bank Dashboard - " + user.getFullName());
        setSize(580, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(FORM_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(18, 20, 18, 20));

        lblWelcome = new JLabel("ABEBA BANK DIGITAL PORTAL", SwingConstants.LEFT);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);

        lblDetails = new JLabel("Member: " + user.getFullName() + "  |  Phone: " + user.getPhoneNumber(),
                SwingConstants.LEFT);
        lblDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetails.setForeground(new Color(148, 163, 184));

        headerPanel.add(lblWelcome);
        headerPanel.add(lblDetails);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Area (Balance Card + Input Card)
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(FORM_BG);

        // Balance Display Card
        JPanel balanceCard = new JPanel(new GridLayout(2, 1));
        balanceCard.setBackground(CARD_BG);
        balanceCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 15, 12, 15)));

        JLabel lblAccHeader = new JLabel("PRIMARY ACCOUNT BALANCE", SwingConstants.CENTER);
        lblAccHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAccHeader.setForeground(new Color(100, 116, 139));

        lblBalance = new JLabel("$0.00", SwingConstants.CENTER);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBalance.setForeground(new Color(16, 185, 129));

        balanceCard.add(lblAccHeader);
        balanceCard.add(lblBalance);
        centerPanel.add(balanceCard);

        // Input Amount Card
        JPanel inputCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        inputCard.setBackground(CARD_BG);
        inputCard.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        JLabel lblAmount = new JLabel("Transaction Amount ($): ");
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAmount.setForeground(new Color(30, 41, 59));

        txtAmount = new JTextField(12);
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        inputCard.add(lblAmount);
        inputCard.add(txtAmount);
        centerPanel.add(inputCard);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Grid Buttons
        JPanel actionGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        actionGrid.setBackground(FORM_BG);

        JButton btnDeposit = createHoverButton("Deposit", DEPOSIT_BASE, DEPOSIT_HOVER);
        JButton btnWithdraw = createHoverButton("Withdraw", WITHDRAW_BASE, WITHDRAW_HOVER);
        JButton btnTransfer = createHoverButton("Transfer", TRANSFER_BASE, TRANSFER_HOVER);
        JButton btnStatement = createHoverButton("Statement", STATEMENT_BASE, STATEMENT_HOVER);
        JButton btnConverter = createHoverButton("Converter", CONVERTER_BASE, CONVERTER_HOVER);
        JButton btnLogout = createHoverButton("Logout", LOGOUT_BASE, LOGOUT_HOVER);

        actionGrid.add(btnDeposit);
        actionGrid.add(btnWithdraw);
        actionGrid.add(btnTransfer);
        actionGrid.add(btnStatement);
        actionGrid.add(btnConverter);
        actionGrid.add(btnLogout);

        mainPanel.add(actionGrid, BorderLayout.SOUTH);

        refreshAccountData();

        btnDeposit.addActionListener(e -> handleDeposit());
        btnWithdraw.addActionListener(e -> handleWithdraw());
        btnTransfer.addActionListener(e -> handleTransfer());
        btnStatement.addActionListener(e -> handleStatement());
        btnConverter.addActionListener(e -> handleConverter());
        btnLogout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JButton createHoverButton(String text, Color baseColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 42));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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

    private void handleDeposit() {
        try {
            BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Enter an amount greater than 0.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal newBalance = currentAccount.getBalance().add(amount);
            if (accountDAO.updateBalanceAndLog(currentAccount.getAccountId(), newBalance, "DEPOSIT", amount)) {
                JOptionPane.showMessageDialog(this, "Deposit Successful: $" + amount);
                refreshAccountData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw() {
        try {
            BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Enter an amount greater than 0.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentAccount.getBalance().compareTo(amount) < 0) {
                JOptionPane.showMessageDialog(this, "Insufficient Funds!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal newBalance = currentAccount.getBalance().subtract(amount);
            if (accountDAO.updateBalanceAndLog(currentAccount.getAccountId(), newBalance, "WITHDRAWAL", amount)) {
                JOptionPane.showMessageDialog(this, "Withdrawal Successful: $" + amount);
                refreshAccountData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTransfer() {
        String recipientAccNum = JOptionPane.showInputDialog(this, "Enter Recipient Account Number (e.g., ACC-1002):");
        if (recipientAccNum == null || recipientAccNum.trim().isEmpty())
            return;

        String amountStr = JOptionPane.showInputDialog(this, "Enter Amount to Transfer ($):");
        if (amountStr == null || amountStr.trim().isEmpty())
            return;

        try {
            BigDecimal amount = new BigDecimal(amountStr.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Enter an amount greater than 0.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = accountDAO.transferFunds(currentUser.getUserId(), recipientAccNum.trim(), amount);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Successfully transferred $" + amount + " to " + recipientAccNum.trim());
                refreshAccountData();
            } else {
                JOptionPane.showMessageDialog(this, "Transfer Failed! Check recipient account number or balance.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleStatement() {
        List<String> records = accountDAO.getStatement(currentAccount.getAccountId());
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transaction history found for this account.", "Statement",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("=== ABEBA BANK TRANSACTION STATEMENT ===\n\n");
        sb.append(String.format("%-24s | %-12s | %-10s\n", "Date / Time", "Type", "Amount"));
        sb.append("----------------------------------------------------------\n");
        for (String record : records) {
            sb.append(record).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString(), 15, 42);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Abeba Bank - Full Statement",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleConverter() {
        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 12));

        JTextField txtConvertAmount = new JTextField(10);
        txtConvertAmount.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        String[] currencies = { "USD ($)", "GBP (£)", "ETB (Birr)" };
        JComboBox<String> cbFrom = new JComboBox<>(currencies);
        JComboBox<String> cbTo = new JComboBox<>(currencies);

        cbFrom.setSelectedIndex(0); // Default From: USD
        cbTo.setSelectedIndex(2); // Default To: ETB

        dialogPanel.add(new JLabel("Amount to Convert:"));
        dialogPanel.add(txtConvertAmount);
        dialogPanel.add(new JLabel("From Currency:"));
        dialogPanel.add(cbFrom);
        dialogPanel.add(new JLabel("To Currency:"));
        dialogPanel.add(cbTo);

        int option = JOptionPane.showConfirmDialog(
                this,
                dialogPanel,
                "Abeba Bank - Currency Converter",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String inputStr = txtConvertAmount.getText().trim();
            if (inputStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(inputStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter an amount greater than zero.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String fromCurrency = (String) cbFrom.getSelectedItem();
                String toCurrency = (String) cbTo.getSelectedItem();

                double rateFrom = getExchangeRate(fromCurrency);
                double rateTo = getExchangeRate(toCurrency);

                double resultAmount = amount * (rateTo / rateFrom);

                String msg = String.format(
                        "Conversion Result:\n\n  %.2f %s = %.2f %s",
                        amount, getCurrencySymbol(fromCurrency),
                        resultAmount, getCurrencySymbol(toCurrency));

                JOptionPane.showMessageDialog(this, msg, "Currency Converter", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double getExchangeRate(String currencyOption) {
        if (currencyOption.contains("USD"))
            return 1.0;
        if (currencyOption.contains("GBP"))
            return 0.78;
        if (currencyOption.contains("ETB"))
            return 125.0;
        return 1.0;
    }

    private String getCurrencySymbol(String currencyOption) {
        if (currencyOption.contains("USD"))
            return "USD";
        if (currencyOption.contains("GBP"))
            return "GBP";
        if (currencyOption.contains("ETB"))
            return "ETB";
        return "";
    }

    private void refreshAccountData() {
        currentAccount = accountDAO.getAccountByUserId(currentUser.getUserId());
        if (currentAccount != null) {
            lblBalance.setText("Acc: " + currentAccount.getAccountNumber() + "  |  $" + currentAccount.getBalance());
            txtAmount.setText("");
        } else {
            lblBalance.setText("Acc: N/A | $0.00");
        }
    }
}