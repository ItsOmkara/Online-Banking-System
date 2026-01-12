package com.banksystem.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.banksystem.util.DatabaseUtil;

/**
 * Controller for withdrawal operations.
 */
@WebServlet("/withdraw")
public class WithdrawController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String atmPin = req.getParameter("atmPin");
        double amount = Double.parseDouble(req.getParameter("amount"));
        String message = "An error occurred. Please try again.";
        String firstName = "", email = "";

        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");

        if (loggedInEmail == null) {
            resp.sendRedirect("home.html");
            return;
        }

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            // Fetch customer details
            String customerQuery = "SELECT c.first_name, c.email FROM customers c " +
                    "JOIN accounts a ON c.customer_id = a.customer_id WHERE c.email = ?";
            try (PreparedStatement customerPs = con.prepareStatement(customerQuery)) {
                customerPs.setString(1, loggedInEmail);
                try (ResultSet customerRs = customerPs.executeQuery()) {
                    if (customerRs.next()) {
                        firstName = customerRs.getString("first_name");
                        email = customerRs.getString("email");
                    } else {
                        firstName = "Unknown";
                        email = "unknown@example.com";
                    }
                }
            }

            // Fetch balance and card number
            String query = "SELECT a.balance, a.cardno FROM accounts a WHERE a.atmpin = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, atmPin);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double currentBalance = rs.getDouble("balance");
                        String cardno = rs.getString("cardno");

                        if (currentBalance >= amount) {
                            String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE atmpin = ?";
                            try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                                updatePs.setDouble(1, amount);
                                updatePs.setString(2, atmPin);

                                int rowsUpdated = updatePs.executeUpdate();
                                if (rowsUpdated > 0) {
                                    String insertQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                                    try (PreparedStatement insertPs = con.prepareStatement(insertQuery)) {
                                        insertPs.setString(1, cardno);
                                        insertPs.setDouble(2, amount);
                                        insertPs.setString(3, "Withdraw");
                                        insertPs.executeUpdate();
                                    }
                                    message = "Withdrawal successful!";
                                } else {
                                    message = "Failed to withdraw amount!";
                                }
                            }
                        } else {
                            message = "Insufficient balance!";
                        }
                    } else {
                        message = "Invalid ATM pin!";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                "&email=" + java.net.URLEncoder.encode(email, "UTF-8") +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }
}
