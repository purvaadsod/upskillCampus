package com.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String accountNumber;
    private String name;
    private final String password;
    private String address;
    private String contact;
    private double balance;
    private final List<Transaction> transactionHistory;

    public UserAccount(String accountNumber, String name, String password, String address, String contact, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.password = password;
        this.address = address;
        this.contact = contact;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add(new Transaction("Initial Deposit", initialDeposit, initialDeposit));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    public void deposit(double amount) {
        this.balance += amount;
        this.transactionHistory.add(new Transaction("Deposit", amount, this.balance));
    }

    public void withdraw(double amount) {
        this.balance -= amount;
        this.transactionHistory.add(new Transaction("Withdrawal", amount, this.balance));
    }

    public void transferOut(double amount, String targetAccount) {
        this.balance -= amount;
        this.transactionHistory.add(new Transaction("Transfer to " + targetAccount, amount, this.balance));
    }

    public void transferIn(double amount, String sourceAccount) {
        this.balance += amount;
        this.transactionHistory.add(new Transaction("Transfer fr " + sourceAccount, amount, this.balance));
    }
}