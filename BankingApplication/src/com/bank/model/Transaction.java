package com.bank.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public record Transaction(String type, double amount, double resultingBalance, String timestamp) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Transaction(String type, double amount, double resultingBalance) {
        this(type, amount, resultingBalance, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    public String toString() {
        return String.format("%-22s | %-15s | %-12.2f | %-15.2f", timestamp, type, amount, resultingBalance);
    }
}