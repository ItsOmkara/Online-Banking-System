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
 * Controller for deposit operations.
 */
@WebServlet("/deposit")
public class DepositController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String atmPin = req.getParameter("atmPin");
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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT a.cardno FROM accounts a " +
                    "JOIN customers c ON a.customer_id = c.customer_id WHERE c.email = ? AND a.atmpin = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ps.setString(2, atmPin);
            rs = ps.executeQuery();

            if (rs.next()) {
                String cardno = rs.getString("cardno");

                String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE atmpin = ?";
                try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                    updatePs.setDouble(1, amount);
                    updatePs.setString(2, atmPin);

                    int rowsUpdated = updatePs.executeUpdate();
                    if (rowsUpdated > 0) {
                        String insertQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                        try (PreparedStatement insertPs = con.prepareStatement(insertQuery)) {
                            insertPs.setString(1, cardno);
                            insertPs.setDouble(2, amount);
                            insertPs.setString(3, "Deposit");
                            insertPs.executeUpdate();
                        }
                        message = "Deposit successful!";
                        System.out.println("Deposit successful for ATM pin: " + atmPin);
                    } else {
                        message = "Failed to deposit amount!";
                    }
                }
            } else {
                message = "Invalid ATM pin!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred. Please try again.";
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseUtil.closeConnection(con);
        }

        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                "&email=" + java.net.URLEncoder.encode(loggedInEmail, "UTF-8") +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }
}
