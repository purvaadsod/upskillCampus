package com.bank.service;

import com.bank.model.UserAccount;
import com.bank.repository.AccountRepository;
import java.util.Map;
import java.util.UUID;

public class BankingService {
    private final AccountRepository repository;
    private final Map<String, UserAccount> database;

    public BankingService() {
        this.repository = new AccountRepository();
        this.database = repository.loadData();
    }

    public UserAccount registerUser(String name, String password, String address, String contact, double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        String accountNumber = "UCT" + Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 8);

        UserAccount account = new UserAccount(accountNumber, name, password, address, contact, initialDeposit);
        database.put(accountNumber, account);
        repository.saveData(database);
        return account;
    }

    public UserAccount authenticate(String accountNumber, String password) {
        UserAccount account = database.get(accountNumber);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    public void deposit(UserAccount account, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        account.deposit(amount);
        repository.saveData(database);
    }

    public void withdraw(UserAccount account, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (account.getBalance() < amount) throw new ArithmeticException("Insufficient balance available.");
        account.withdraw(amount);
        repository.saveData(database);
    }

    public void transfer(UserAccount source, String destAccountNumber, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive.");
        if (source.getAccountNumber().equals(destAccountNumber)) throw new IllegalArgumentException("Cannot transfer funds to yourself.");

        UserAccount target = database.get(destAccountNumber);
        if (target == null) throw new IllegalArgumentException("Target account number not found.");
        if (source.getBalance() < amount) throw new ArithmeticException("Insufficient balance for transfer.");

        source.transferOut(amount, destAccountNumber);
        target.transferIn(amount, source.getAccountNumber());
        repository.saveData(database);
    }

    public void updateProfile(UserAccount account, String name, String address, String contact) {
        account.setName(name);
        account.setAddress(address);
        account.setContact(contact);
        repository.saveData(database);
    }
}