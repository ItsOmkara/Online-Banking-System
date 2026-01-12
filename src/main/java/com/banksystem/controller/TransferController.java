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
 * Controller for transfer operations.
 */
@WebServlet("/transfer")
public class TransferController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderAtmPin = req.getParameter("atmPin");
        String recipientCardNo = req.getParameter("accountNumber");
        double amount = Double.parseDouble(req.getParameter("amount"));
        String message = "An error occurred. Please try again.";

        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");
        String firstName = (String) session.getAttribute("firstName");

        if (loggedInEmail == null || firstName == null) {
            resp.sendRedirect("home.html");
            return;
        }

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            // Fetch sender details
            String senderQuery = "SELECT a.balance, a.cardno FROM accounts a " +
                    "JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE a.atmpin = ? AND c.email = ?";
            try (PreparedStatement senderPs = con.prepareStatement(senderQuery)) {
                senderPs.setString(1, senderAtmPin);
                senderPs.setString(2, loggedInEmail);

                try (ResultSet senderRs = senderPs.executeQuery()) {
                    if (senderRs.next()) {
                        double senderBalance = senderRs.getDouble("balance");
                        String senderCardNo = senderRs.getString("cardno");

                        if (senderBalance >= amount) {
                            // Verify recipient exists
                            String recipientQuery = "SELECT cardno FROM accounts WHERE cardno = ?";
                            try (PreparedStatement recipientPs = con.prepareStatement(recipientQuery)) {
                                recipientPs.setString(1, recipientCardNo);

                                try (ResultSet recipientRs = recipientPs.executeQuery()) {
                                    if (recipientRs.next()) {
                                        // Deduct from sender
                                        String deductQuery = "UPDATE accounts SET balance = balance - ? WHERE cardno = ?";
                                        try (PreparedStatement deductPs = con.prepareStatement(deductQuery)) {
                                            deductPs.setDouble(1, amount);
                                            deductPs.setString(2, senderCardNo);
                                            deductPs.executeUpdate();
                                        }

                                        // Add to recipient
                                        String addQuery = "UPDATE accounts SET balance = balance + ? WHERE cardno = ?";
                                        try (PreparedStatement addPs = con.prepareStatement(addQuery)) {
                                            addPs.setDouble(1, amount);
                                            addPs.setString(2, recipientCardNo);
                                            addPs.executeUpdate();
                                        }

                                        // Record sender transaction
                                        String senderTxQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                                        try (PreparedStatement senderTxPs = con.prepareStatement(senderTxQuery)) {
                                            senderTxPs.setString(1, senderCardNo);
                                            senderTxPs.setDouble(2, amount);
                                            senderTxPs.setString(3, "Transfer Out");
                                            senderTxPs.executeUpdate();
                                        }

                                        // Record recipient transaction
                                        String recipientTxQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                                        try (PreparedStatement recipientTxPs = con.prepareStatement(recipientTxQuery)) {
                                            recipientTxPs.setString(1, recipientCardNo);
                                            recipientTxPs.setDouble(2, amount);
                                            recipientTxPs.setString(3, "Transfer In");
                                            recipientTxPs.executeUpdate();
                                        }

                                        message = "Transfer successful!";
                                    } else {
                                        message = "Recipient's account not found!";
                                    }
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
            message = "An error occurred. Please try again.";
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                "&email=" + java.net.URLEncoder.encode(loggedInEmail, "UTF-8") +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }
}
