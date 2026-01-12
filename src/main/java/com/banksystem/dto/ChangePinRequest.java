package com.banksystem.dto;

/**
 * DTO for Change PIN request.
 */
public class ChangePinRequest {
    private String cardNo;
    private String oldPin;
    private String newPin;

    // Getters and Setters
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
