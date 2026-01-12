package com.banksystem.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.banksystem.model.Account;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for fetching account data by email.
 */
@WebServlet("/FetchAccountDataServlet")
public class AccountDataController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String email = req.getParameter("email");
        Account account = new Account();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT c.first_name, c.last_name, c.mobile_number, c.branch, a.cardno, a.atmpin, a.accounttype "
                    +
                    "FROM customers c " +
                    "JOIN accounts a ON c.customer_id = a.customer_id " +
                    "WHERE c.email = ?";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        account.setFirstName(rs.getString("first_name"));
                        account.setLastName(rs.getString("last_name"));
                        account.setMobileNumber(rs.getString("mobile_number"));
                        account.setBranch(rs.getString("branch"));
                        account.setCardNo(rs.getString("cardno"));
                        account.setAtmPin(rs.getString("atmpin"));
                        account.setAccountType(rs.getString("accounttype"));
                    } else {
                        account.setErrorMessage("No account data found for the given email.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            account.setErrorMessage("An error occurred while fetching account data.");
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(account));
        out.flush();
    }
}
