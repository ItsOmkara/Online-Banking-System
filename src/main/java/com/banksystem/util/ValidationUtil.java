package com.banksystem.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 * Centralizes validation logic for email, password, and other inputs.
 */
public class ValidationUtil {

    // Email pattern - accepts Gmail addresses only
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:gmail\\.com)$");

    // Password pattern - at least 8 chars with one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,}$");

    /**
     * Validate email format (Gmail addresses only).
     * 
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate password format.
     * Password must be at least 8 characters long and contain at least one special
     * character.
     * 
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Check if a string is null or empty.
     * 
     * @param str the string to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
