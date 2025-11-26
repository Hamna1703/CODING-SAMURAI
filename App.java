import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class App {
    // Simple in-memory storage for demo purposes
    private static Map<String, Account> accounts = new HashMap<>();
    private static String currentAccountNumber = null;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame());
    }
    
    public static Map<String, Account> getAccounts() {
        return accounts;
    }
    
    public static void setCurrentAccount(String accountNumber) {
        currentAccountNumber = accountNumber;
    }
    
    public static String getCurrentAccount() {
        return currentAccountNumber;
    }
}

class Account {
    private String accountNumber;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String address;
    private String dob;
    private String accountType;
    private String maritalStatus;
    private double balance;
    private String pin;
    
    public Account(String accountNumber, String name, String email, String mobile, 
                  String gender, String address, String dob, String accountType, 
                  String maritalStatus) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.accountType = accountType;
        this.maritalStatus = maritalStatus;
        this.balance = 1000.0; // Starting balance
        this.pin = generateRandomPin();
    }
    
    private String generateRandomPin() {
        Random rand = new Random();
        return String.format("%04d", rand.nextInt(10000));
    }
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getDob() { return dob; }
    public String getAccountType() { return accountType; }
    public String getMaritalStatus() { return maritalStatus; }
    public double getBalance() { return balance; }
    public String getPin() { return pin; }
    
    public void setBalance(double balance) { this.balance = balance; }
    public void setPin(String pin) { this.pin = pin; }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}

class WelcomeFrame extends JFrame {
    public WelcomeFrame() {
        setTitle("Welcome to Chota ATM");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Chota ATM System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton createAccBtn = new JButton("Create Account");
        JButton viewInfoBtn = new JButton("View Info");
        JButton openATMBtn = new JButton("Open ATM");
        JButton cardDetailsBtn = new JButton("Card Details");
        
        // Style buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        createAccBtn.setFont(buttonFont);
        viewInfoBtn.setFont(buttonFont);
        openATMBtn.setFont(buttonFont);
        cardDetailsBtn.setFont(buttonFont);

        buttonPanel.add(createAccBtn);
        buttonPanel.add(viewInfoBtn);
        buttonPanel.add(openATMBtn);
        buttonPanel.add(cardDetailsBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        createAccBtn.addActionListener(e -> new CreateAccountFrame());
        viewInfoBtn.addActionListener(e -> new ViewInfoFrame());
        openATMBtn.addActionListener(e -> new LoginFrame());
        cardDetailsBtn.addActionListener(e -> new CardDetailsFrame());

        setVisible(true);
    }
}

class CreateAccountFrame extends JFrame {
    public CreateAccountFrame() {
        setTitle("Create Account");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 2, 5, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField mobile = new JTextField();
        JRadioButton male = new JRadioButton("Male"), female = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup(); 
        genderGroup.add(male); 
        genderGroup.add(female);
        JTextField address = new JTextField();
        JTextField dob = new JTextField();
        JComboBox<String> accType = new JComboBox<>(new String[]{"Savings", "Current"});
        JComboBox<String> maritalStatus = new JComboBox<>(new String[]{"Single", "Married"});

        add(new JLabel("Full Name:")); add(name);
        add(new JLabel("Email:")); add(email);
        add(new JLabel("Mobile No:")); add(mobile);
        add(new JLabel("Gender:")); 
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(male); genderPanel.add(female);
        add(genderPanel);
        add(new JLabel("Address:")); add(address);
        add(new JLabel("DOB (DD/MM/YYYY):")); add(dob);
        add(new JLabel("Account Type:")); add(accType);
        add(new JLabel("Marital Status:")); add(maritalStatus);
        add(new JLabel(""));
        add(new JLabel(""));

        JButton submit = new JButton("Submit"), exit = new JButton("Exit");
        add(submit); add(exit);

        exit.addActionListener(e -> dispose());
        submit.addActionListener(e -> {
            if (validateInput(name, email, mobile, genderGroup, address, dob)) {
                String accountNumber = generateAccountNumber();
                String gender = male.isSelected() ? "Male" : "Female";
                
                Account account = new Account(
                    accountNumber,
                    name.getText().trim(),
                    email.getText().trim(),
                    mobile.getText().trim(),
                    gender,
                    address.getText().trim(),
                    dob.getText().trim(),
                    (String) accType.getSelectedItem(),
                    (String) maritalStatus.getSelectedItem()
                );
                
                App.getAccounts().put(accountNumber, account);
                
                JOptionPane.showMessageDialog(this, 
                    "Account Created Successfully!\n" +
                    "Account Number: " + accountNumber + "\n" +
                    "PIN: " + account.getPin() + "\n" +
                    "Initial Balance: $" + account.getBalance(),
                    "Account Created", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        setVisible(true);
    }
    
    private boolean validateInput(JTextField name, JTextField email, JTextField mobile, 
                                ButtonGroup genderGroup, JTextField address, JTextField dob) {
        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name.");
            return false;
        }
        if (email.getText().trim().isEmpty() || !email.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return false;
        }
        if (mobile.getText().trim().isEmpty() || mobile.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(this, "Please enter a valid mobile number (at least 10 digits).");
            return false;
        }
        if (genderGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select your gender.");
            return false;
        }
        if (address.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your address.");
            return false;
        }
        if (dob.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your date of birth.");
            return false;
        }
        return true;
    }
    
    private String generateAccountNumber() {
        Random rand = new Random();
        return "ACC" + String.format("%06d", rand.nextInt(1000000));
    }
}

class ATMMenuFrame extends JFrame {
    private Account currentAccount;
    
    public ATMMenuFrame(Account account) {
        this.currentAccount = account;
        setTitle("ATM Menu - Welcome " + account.getName());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome, " + account.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton balanceBtn = new JButton("Balance Enquiry");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton pinBtn = new JButton("Change PIN");
        JButton fundBtn = new JButton("Fund Transfer");
        JButton cancelBtn = new JButton("Logout");
        
        // Style buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        balanceBtn.setFont(buttonFont);
        withdrawBtn.setFont(buttonFont);
        depositBtn.setFont(buttonFont);
        pinBtn.setFont(buttonFont);
        fundBtn.setFont(buttonFont);
        cancelBtn.setFont(buttonFont);

        buttonPanel.add(balanceBtn); buttonPanel.add(withdrawBtn);
        buttonPanel.add(depositBtn); buttonPanel.add(pinBtn);
        buttonPanel.add(fundBtn); buttonPanel.add(cancelBtn);
        
        add(welcomePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners
        balanceBtn.addActionListener(e -> showBalance());
        withdrawBtn.addActionListener(e -> withdraw());
        depositBtn.addActionListener(e -> deposit());
        pinBtn.addActionListener(e -> changePin());
        fundBtn.addActionListener(e -> fundTransfer());
        cancelBtn.addActionListener(e -> {
            App.setCurrentAccount(null);
            dispose();
            new WelcomeFrame();
        });

        setVisible(true);
    }
    
    private void showBalance() {
        JOptionPane.showMessageDialog(this, 
            "Account Number: " + currentAccount.getAccountNumber() + "\n" +
            "Current Balance: $" + String.format("%.2f", currentAccount.getBalance()),
            "Balance Enquiry", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void withdraw() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (currentAccount.withdraw(amount)) {
                    JOptionPane.showMessageDialog(this, 
                        "Withdrawal Successful!\n" +
                        "Amount Withdrawn: $" + String.format("%.2f", amount) + "\n" +
                        "Remaining Balance: $" + String.format("%.2f", currentAccount.getBalance()));
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                currentAccount.deposit(amount);
                JOptionPane.showMessageDialog(this, 
                    "Deposit Successful!\n" +
                    "Amount Deposited: $" + String.format("%.2f", amount) + "\n" +
                    "New Balance: $" + String.format("%.2f", currentAccount.getBalance()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void changePin() {
        String currentPin = JOptionPane.showInputDialog(this, "Enter current PIN:");
        if (currentPin != null && currentPin.equals(currentAccount.getPin())) {
            String newPin = JOptionPane.showInputDialog(this, "Enter new 4-digit PIN:");
            if (newPin != null && newPin.length() == 4 && newPin.matches("\\d{4}")) {
                currentAccount.setPin(newPin);
                JOptionPane.showMessageDialog(this, "PIN changed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid 4-digit PIN!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (currentPin != null) {
            JOptionPane.showMessageDialog(this, "Incorrect current PIN!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fundTransfer() {
        String targetAccount = JOptionPane.showInputDialog(this, "Enter target account number:");
        if (targetAccount != null && !targetAccount.trim().isEmpty()) {
            if (App.getAccounts().containsKey(targetAccount)) {
                String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
                if (amountStr != null && !amountStr.trim().isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        if (currentAccount.withdraw(amount)) {
                            Account targetAcc = App.getAccounts().get(targetAccount);
                            targetAcc.deposit(amount);
                            JOptionPane.showMessageDialog(this, 
                                "Transfer Successful!\n" +
                                "Amount Transferred: $" + String.format("%.2f", amount) + "\n" +
                                "To Account: " + targetAccount + "\n" +
                                "Remaining Balance: $" + String.format("%.2f", currentAccount.getBalance()));
                        } else {
                            JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount!", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid amount!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Target account not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("ATM Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextField accountField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        
        add(new JLabel("Account Number:"));
        add(accountField);
        add(new JLabel("PIN:"));
        add(pinField);
        add(new JLabel(""));
        add(new JLabel(""));
        
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");
        
        add(loginBtn);
        add(cancelBtn);
        
        loginBtn.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            String pin = new String(pinField.getPassword());
            
            Account account = App.getAccounts().get(accountNumber);
            if (account != null && account.getPin().equals(pin)) {
                App.setCurrentAccount(accountNumber);
                dispose();
                new ATMMenuFrame(account);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid account number or PIN!", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                pinField.setText("");
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        setVisible(true);
    }
}

class ViewInfoFrame extends JFrame {
    public ViewInfoFrame() {
        setTitle("View Account Information");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextField accountField = new JTextField();
        
        add(new JLabel("Account Number:"));
        add(accountField);
        add(new JLabel(""));
        add(new JLabel(""));
        
        JButton viewBtn = new JButton("View Info");
        JButton cancelBtn = new JButton("Cancel");
        
        add(viewBtn);
        add(cancelBtn);
        
        viewBtn.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            Account account = App.getAccounts().get(accountNumber);
            
            if (account != null) {
                String info = "Account Information:\n\n" +
                    "Account Number: " + account.getAccountNumber() + "\n" +
                    "Name: " + account.getName() + "\n" +
                    "Email: " + account.getEmail() + "\n" +
                    "Mobile: " + account.getMobile() + "\n" +
                    "Gender: " + account.getGender() + "\n" +
                    "Address: " + account.getAddress() + "\n" +
                    "DOB: " + account.getDob() + "\n" +
                    "Account Type: " + account.getAccountType() + "\n" +
                    "Marital Status: " + account.getMaritalStatus() + "\n" +
                    "Balance: $" + String.format("%.2f", account.getBalance());
                    
                JOptionPane.showMessageDialog(this, info, "Account Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Account not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        setVisible(true);
    }
}

class CardDetailsFrame extends JFrame {
    public CardDetailsFrame() {
        setTitle("Card Details");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextField accountField = new JTextField();
        
        add(new JLabel("Account Number:"));
        add(accountField);
        add(new JLabel(""));
        add(new JLabel(""));
        
        JButton viewBtn = new JButton("View Card Details");
        JButton cancelBtn = new JButton("Cancel");
        
        add(viewBtn);
        add(cancelBtn);
        
        viewBtn.addActionListener(e -> {
            String accountNumber = accountField.getText().trim();
            Account account = App.getAccounts().get(accountNumber);
            
            if (account != null) {
                Random rand = new Random();
                String cardNumber = String.format("%04d-%04d-%04d-%04d", 
                    rand.nextInt(10000), rand.nextInt(10000), 
                    rand.nextInt(10000), rand.nextInt(10000));
                String expiryDate = "12/" + (java.time.Year.now().getValue() + 5);
                String cvv = String.format("%03d", rand.nextInt(1000));
                
                String cardInfo = "Card Details:\n\n" +
                    "Card Number: " + cardNumber + "\n" +
                    "Cardholder Name: " + account.getName().toUpperCase() + "\n" +
                    "Expiry Date: " + expiryDate + "\n" +
                    "CVV: " + cvv + "\n" +
                    "Card Type: " + account.getAccountType() + " Card";
                    
                JOptionPane.showMessageDialog(this, cardInfo, "Card Details", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Account not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        setVisible(true);
    }
}
