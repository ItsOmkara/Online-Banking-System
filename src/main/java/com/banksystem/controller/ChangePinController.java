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
import com.banksystem.dto.ChangePinRequest;
import com.banksystem.dto.ApiResponse;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for changing ATM PIN.
 */
@WebServlet("/ChangePinServlet")
public class ChangePinController extends HttpServlet {

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

        ChangePinRequest changePinRequest = gson.fromJson(requestBody.toString(), ChangePinRequest.class);
        String cardNo = changePinRequest.getCardNo();
        String oldPin = changePinRequest.getOldPin();
        String newPin = changePinRequest.getNewPin();
        String message;

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            // Check if old PIN is correct
            String checkPinQuery = "SELECT * FROM accounts WHERE cardno = ? AND atmpin = ?";
            try (PreparedStatement checkPinStmt = con.prepareStatement(checkPinQuery)) {
                checkPinStmt.setString(1, cardNo);
                checkPinStmt.setString(2, oldPin);

                try (ResultSet rs = checkPinStmt.executeQuery()) {
                    if (rs.next()) {
                        // Update PIN
                        String updatePinQuery = "UPDATE accounts SET atmpin = ? WHERE cardno = ?";
                        try (PreparedStatement updatePinStmt = con.prepareStatement(updatePinQuery)) {
                            updatePinStmt.setString(1, newPin);
                            updatePinStmt.setString(2, cardNo);

                            int rowsUpdated = updatePinStmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                message = "PIN changed successfully!";
                            } else {
                                message = "Failed to change PIN.";
                            }
                        }
                    } else {
                        message = "Incorrect old PIN.";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred while changing the PIN.";
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(new ApiResponse(message)));
        out.flush();
    }
}
