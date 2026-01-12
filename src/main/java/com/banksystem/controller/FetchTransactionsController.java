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

import com.google.gson.Gson;
import com.banksystem.model.Transaction;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for fetching all transactions (admin view).
 */
@WebServlet("/fetchTransactions")
public class FetchTransactionsController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Transaction> transactions = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT t.transaction_id, t.cardno, t.amount, t.transaction_type, t.transaction_date, " +
                    "c.first_name, c.last_name, c.mobile_number, c.email " +
                    "FROM transactions t " +
                    "INNER JOIN accounts a ON t.cardno = a.cardno " +
                    "INNER JOIN customers c ON a.customer_id = c.customer_id";

            try (PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Transaction tx = new Transaction();
                    tx.setTransactionId(rs.getInt("transaction_id"));
                    tx.setCardNo(rs.getString("cardno"));
                    tx.setAmount(rs.getDouble("amount"));
                    tx.setTransactionType(rs.getString("transaction_type"));
                    tx.setTransactionDate(rs.getString("transaction_date"));
                    tx.setFirstName(rs.getString("first_name"));
                    tx.setLastName(rs.getString("last_name"));
                    tx.setMobileNumber(rs.getString("mobile_number"));
                    tx.setEmail(rs.getString("email"));
                    transactions.add(tx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(transactions));
        out.flush();
    }
}
