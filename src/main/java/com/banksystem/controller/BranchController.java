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
import com.banksystem.model.Branch;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for branch operations.
 */
@WebServlet("/fetchBranches")
public class BranchController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Branch> branches = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT branch_name FROM branches";
            try (PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Branch branch = new Branch(rs.getString("branch_name"));
                    branches.add(branch);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(branches));
        out.flush();
    }
}
