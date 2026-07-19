package com.bank.repository;

import com.bank.model.UserAccount;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class AccountRepository {
    private static final String FILE_NAME = "banking_data.dat";

    @SuppressWarnings("unchecked")
    public Map<String, UserAccount> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, UserAccount>) ois.readObject();
        } catch (Exception e) {
            System.err.println("[System Notice] Storage workspace clean. Initializing new system state.");
            return new ConcurrentHashMap<>();
        }
    }

    public synchronized void saveData(Map<String, UserAccount> database) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(database);
        } catch (IOException e) {
            System.err.println("[System Error] Critical disk write failure: " + e.getMessage());
        }
    }
}