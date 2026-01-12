package com.banksystem.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for approving checkbook requests (admin).
 */
@WebServlet("/approveCheckbook")
public class ApproveCheckbookController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            int accountId = jsonObject.get("accountId").getAsInt();

            Connection con = DatabaseUtil.getConnection();

            String query = "UPDATE accounts SET checkbook_status = 'Approved' WHERE account_id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, accountId);
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    out.print("{\"success\": true}");
                } else {
                    out.print("{\"success\": false}");
                }
            }

            DatabaseUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false}");
        }

        out.flush();
    }
}
