package com.banksystem.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.banksystem.model.Transaction;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for fetching transaction history.
 */
@WebServlet("/TransactionHistoryServlet")
public class TransactionHistoryController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");

        if (loggedInEmail == null) {
            resp.sendRedirect("home.html");
            return;
        }

        List<Transaction> transactions = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT t.transaction_id, t.amount, t.cardno, t.transaction_type, t.transaction_date " +
                    "FROM transactions t " +
                    "JOIN accounts a ON t.cardno = a.cardno " +
                    "JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE c.email = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, loggedInEmail);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Transaction transaction = new Transaction(
                                rs.getInt("transaction_id"),
                                rs.getDouble("amount"),
                                rs.getString("cardno"),
                                rs.getString("transaction_type"),
                                rs.getTimestamp("transaction_date").toString());
                        transactions.add(transaction);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(transactions));
    }
}
