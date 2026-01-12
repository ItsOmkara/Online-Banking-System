package com.banksystem.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.banksystem.util.DatabaseUtil;

/**
 * Controller for adding new branches.
 */
@WebServlet("/addBranch")
public class AddBranchController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String branchName = req.getParameter("branchName");
        String branchCode = req.getParameter("branchCode");
        String branchAddress = req.getParameter("branchAddress");
        String message;

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "INSERT INTO branches (branch_name, branch_code, branch_address) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, branchName);
                ps.setString(2, branchCode);
                ps.setString(3, branchAddress);

                int count = ps.executeUpdate();
                if (count > 0) {
                    message = "Branch added successfully!";
                } else {
                    message = "Failed to add Branch!";
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
