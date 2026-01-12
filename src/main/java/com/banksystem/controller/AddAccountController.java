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

import com.banksystem.util.DatabaseUtil;

/**
 * Controller for adding new accounts.
 */
@WebServlet("/addAccount")
public class AddAccountController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String accountType = req.getParameter("accountType");
        String cardNo = req.getParameter("cardNumber");
        String atmPin = req.getParameter("atmPin");
        String message;

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            // Check if customer exists
            String checkCustomerQuery = "SELECT customer_id FROM customers WHERE customer_id = ?";
            try (PreparedStatement checkCustomerPs = con.prepareStatement(checkCustomerQuery)) {
                checkCustomerPs.setInt(1, customerId);

                try (ResultSet rs = checkCustomerPs.executeQuery()) {
                    if (!rs.next()) {
                        message = "Customer ID does not exist!";
                    } else {
                        // Check if card number already exists
                        String checkCardQuery = "SELECT cardno FROM accounts WHERE cardno = ?";
                        try (PreparedStatement checkCardPs = con.prepareStatement(checkCardQuery)) {
                            checkCardPs.setString(1, cardNo);

                            try (ResultSet cardRs = checkCardPs.executeQuery()) {
                                if (cardRs.next()) {
                                    message = "Card number already exists!";
                                } else {
                                    // Insert new account
                                    String insertQuery = "INSERT INTO accounts (customer_id, accounttype, cardno, atmpin, balance) VALUES (?, ?, ?, ?, 0.00)";
                                    try (PreparedStatement insertPs = con.prepareStatement(insertQuery)) {
                                        insertPs.setInt(1, customerId);
                                        insertPs.setString(2, accountType);
                                        insertPs.setString(3, cardNo);
                                        insertPs.setString(4, atmPin);

                                        int count = insertPs.executeUpdate();
                                        if (count > 0) {
                                            message = "Account added successfully!";
                                        } else {
                                            message = "Failed to add account!";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred: " + e.getMessage();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}
