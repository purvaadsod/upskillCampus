package com.bank;

import com.bank.model.Transaction;
import com.bank.model.UserAccount;
import com.bank.service.BankingService;
import java.io.Console;
import java.util.Scanner;

public class BankingApplication {
    private static final BankingService bankService = new BankingService();
    private static final Scanner scanner = new Scanner(System.in);
    private static UserAccount currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("     UNICONVERGE CORE BANKING    ");
            System.out.println("=================================");
            System.out.println("1. New User Registration");
            System.out.println("2. Existing User Secure Login");
            System.out.println("3. Exit System");
            System.out.print("Please choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> registerUser();
                case "2" -> loginUser();
                case "3" -> {
                    System.out.println("Thank you for banking with UniConverge. Goodbye.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please select from options 1-3.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("\n--- NEW USER REGISTRATION ---");
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        String password = readSecurePassword("Create Password: ");
        System.out.print("Enter Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Enter Contact Number: ");
        String contact = scanner.nextLine().trim();

        double initialDeposit = -1;
        while (initialDeposit < 0) {
            System.out.print("Enter Initial Deposit Amount (INR): ");
            try {
                initialDeposit = Double.parseDouble(scanner.nextLine());
                if (initialDeposit < 0) System.out.println("Deposit cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid numerical formatting. Try again.");
            }
        }

        try {
            UserAccount account = bankService.registerUser(name, password, address, contact, initialDeposit);
            System.out.println("\n[SUCCESS] Registration Complete.");
            System.out.println("YOUR ACCOUNT NUMBER IS: " + account.getAccountNumber());
            System.out.println("Please record this securely.");
        } catch (Exception e) {
            System.out.println("[REGISTRATION FAILED] " + e.getMessage());
        }
    }

    private static void loginUser() {
        System.out.println("\n--- SECURE LOGIN ---");
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine().trim();
        String pass = readSecurePassword("Enter Password: ");

        currentUser = bankService.authenticate(accNum, pass);
        if (currentUser != null) {
            System.out.println("\nLogin successful. Welcome back, " + currentUser.getName() + ".");
            userDashboard();
        } else {
            System.out.println("\n[ERROR] Invalid Account Number or Password.");
        }
    }

    private static void userDashboard() {
        while (currentUser != null) {
            System.out.println("\n--- MAIN ACCOUNT OPERATIONS ---");
            System.out.println("1. View/Update Profile");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Fund Transfer");
            System.out.println("5. Print Statement Ledger");
            System.out.println("6. Logout");
            System.out.print("Select an operation: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> manageAccount();
                case "2" -> performDeposit();
                case "3" -> performWithdrawal();
                case "4" -> performTransfer();
                case "5" -> printStatement();
                case "6" -> {
                    System.out.println("Logging you out safely...");
                    currentUser = null;
                }
                default -> System.out.println("Invalid command. Please select options 1-6.");
            }
        }
    }

    private static void manageAccount() {
        System.out.println("\n--- ACCOUNT PROFILE ---");
        System.out.println("Account Number: " + currentUser.getAccountNumber());
        System.out.println("1. Name: " + currentUser.getName());
        System.out.println("2. Address: " + currentUser.getAddress());
        System.out.println("3. Contact Info: " + currentUser.getContact());
        System.out.printf("Current Balance: %.2f INR\n", currentUser.getBalance());

        System.out.print("\nDo you want to edit your profile info? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.print("Enter New Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter New Address: ");
            String address = scanner.nextLine().trim();
            System.out.print("Enter New Contact: ");
            String contact = scanner.nextLine().trim();

            bankService.updateProfile(currentUser, name, address, contact);
            System.out.println("[SUCCESS] Profile modified successfully.");
        }
    }

    private static void performDeposit() {
        System.out.print("\nEnter amount to deposit (INR): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine());
            bankService.deposit(currentUser, amt);
            System.out.printf("[SUCCESS] Deposited: %.2f INR | Current Balance: %.2f INR\n", amt, currentUser.getBalance());
        } catch (Exception e) {
            System.out.println("[TRANSACTION REJECTED] " + e.getMessage());
        }
    }

    private static void performWithdrawal() {
        System.out.print("\nEnter amount to withdraw (INR): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine());
            bankService.withdraw(currentUser, amt);
            System.out.printf("[SUCCESS] Withdrawn: %.2f INR | Remaining Balance: %.2f INR\n", amt, currentUser.getBalance());
        } catch (Exception e) {
            System.out.println("[TRANSACTION REJECTED] " + e.getMessage());
        }
    }

    private static void performTransfer() {
        System.out.print("\nEnter destination Account Number: ");
        String destAcc = scanner.nextLine().trim();
        System.out.print("Enter amount to transfer (INR): ");
        try {
            double amt = Double.parseDouble(scanner.nextLine());
            bankService.transfer(currentUser, destAcc, amt);
            System.out.printf("[SUCCESS] Sent: %.2f INR | Current Balance: %.2f INR\n", amt, currentUser.getBalance());
        } catch (Exception e) {
            System.out.println("[TRANSFER FAILED] " + e.getMessage());
        }
    }

    private static void printStatement() {
        System.out.println("\n==========================================================================");
        System.out.println("                       ACCOUNT TRANSACTION STATEMENT                      ");
        System.out.println("==========================================================================");
        System.out.printf("%-22s | %-15s | %-12s | %-15s\n", "Timestamp", "Transaction Type", "Amount", "Current Balance");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction tx : currentUser.getTransactionHistory()) {
            System.out.println(tx);
        }
        System.out.println("==========================================================================");
    }

    private static String readSecurePassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            return new String(console.readPassword(prompt));
        }
        System.out.print(prompt);
        return scanner.nextLine();
    }
}