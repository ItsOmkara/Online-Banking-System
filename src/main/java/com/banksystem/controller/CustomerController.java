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
import com.banksystem.model.Customer;
import com.banksystem.util.DatabaseUtil;

/**
 * Controller for fetching customers list.
 */
@WebServlet("/testAdminView")
public class CustomerController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Customer> customers = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT * FROM customers";
            try (PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("date_of_birth"),
                            rs.getString("mobile_number"),
                            rs.getString("email"),
                            rs.getString("gender"),
                            rs.getString("branch"),
                            rs.getString("password"),
                            rs.getString("permanent_address"),
                            rs.getString("present_address"));
                    customers.add(customer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        out.print(gson.toJson(customers));
        out.flush();
    }
}
