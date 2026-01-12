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
import com.banksystem.dto.ApiResponse;
import com.banksystem.dto.CheckbookRequest;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for checkbook operations.
 */
@WebServlet("/applyForCheckbook")
public class CheckbookController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Parse request body
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            requestBody.append(line);
        }

        CheckbookRequest request = gson.fromJson(requestBody.toString(), CheckbookRequest.class);
        String cardNo = request.getCardNo();
        String message;

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "UPDATE accounts SET checkbook_status = 'Pending' WHERE cardno = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, cardNo);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    message = "Checkbook application submitted successfully!";
                } else {
                    message = "Failed to submit checkbook application.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred while submitting the checkbook application.";
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(new ApiResponse(message)));
        out.flush();
    }
}
