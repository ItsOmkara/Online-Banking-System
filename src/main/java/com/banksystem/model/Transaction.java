package com.banksystem.model;

/**
 * Transaction entity representing a bank transaction.
 */
public class Transaction {
    private int transactionId;
    private double amount;
    private String cardNo;
    private String transactionType;
    private String transactionDate;

    // Customer info (for joined queries)
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;

    // Default constructor
    public Transaction() {
    }

    // Constructor for basic transaction
    public Transaction(int transactionId, double amount, String cardNo,
            String transactionType, String transactionDate) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.cardNo = cardNo;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
