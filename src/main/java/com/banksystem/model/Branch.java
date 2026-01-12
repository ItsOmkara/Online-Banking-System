package com.banksystem.model;

/**
 * Branch entity representing a bank branch.
 */
public class Branch {
    private String branchName;
    private String branchCode;
    private String branchAddress;

    // Default constructor
    public Branch() {
    }

    // Constructor with name only
    public Branch(String branchName) {
        this.branchName = branchName;
    }

    // Full constructor
    public Branch(String branchName, String branchCode, String branchAddress) {
        this.branchName = branchName;
        this.branchCode = branchCode;
        this.branchAddress = branchAddress;
    }

    // Getters and Setters
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }
}
