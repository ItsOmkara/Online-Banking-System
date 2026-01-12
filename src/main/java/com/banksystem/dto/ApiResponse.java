package com.banksystem.dto;

/**
 * Generic API response wrapper for JSON responses.
 */
public class ApiResponse {
    private String message;
    private boolean success;

    // Default constructor
    public ApiResponse() {
    }

    // Constructor with message only
    public ApiResponse(String message) {
        this.message = message;
        this.success = true;
    }

    // Full constructor
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
