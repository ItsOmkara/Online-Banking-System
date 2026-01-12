package com.banksystem.dto;

/**
 * DTO for Checkbook application request.
 */
public class CheckbookRequest {
    private String cardNo;
    private int accountId;

    // Getters and Setters
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
