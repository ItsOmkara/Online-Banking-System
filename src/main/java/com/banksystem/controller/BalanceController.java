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
 * Controller for checking account balance.
 */
@WebServlet("/CheckBalanceServlet")
public class BalanceController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String atmPin = req.getParameter("atmPin");
        double balance = 0.0;
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

            String query = "SELECT a.balance FROM accounts a " +
                    "JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE c.email = ? AND a.atmpin = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, loggedInEmail);
                ps.setString(2, atmPin);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        balance = rs.getDouble("balance");
                        message = "Balance fetched successfully!";
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
                "&balance=" + balance +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }
}
