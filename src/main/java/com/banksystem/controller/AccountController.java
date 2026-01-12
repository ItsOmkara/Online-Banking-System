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
import com.banksystem.model.Account;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for fetching accounts data.
 */
@WebServlet("/fetchAccounts")
public class AccountController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Account> accounts = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT a.account_id, a.customer_id, a.cardno, a.atmpin, a.accounttype, a.balance, " +
                    "a.checkbook_status, c.first_name, c.last_name, c.mobile_number, c.email " +
                    "FROM accounts a " +
                    "INNER JOIN customers c ON a.customer_id = c.customer_id";

            try (PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Account acc = new Account();
                    acc.setAccountId(rs.getInt("account_id"));
                    acc.setCustomerId(rs.getInt("customer_id"));
                    acc.setCardNo(rs.getString("cardno"));
                    acc.setAtmPin(rs.getString("atmpin"));
                    acc.setAccountType(rs.getString("accounttype"));
                    acc.setBalance(rs.getDouble("balance"));
                    acc.setFirstName(rs.getString("first_name"));
                    acc.setLastName(rs.getString("last_name"));
                    acc.setMobileNumber(rs.getString("mobile_number"));
                    acc.setEmail(rs.getString("email"));
                    acc.setCheckbookStatus(rs.getString("checkbook_status"));
                    accounts.add(acc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(accounts));
        out.flush();
    }
}
