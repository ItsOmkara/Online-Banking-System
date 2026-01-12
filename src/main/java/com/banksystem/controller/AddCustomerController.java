package com.banksystem.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.banksystem.util.DatabaseUtil;

/**
 * Controller for adding new customers.
 */
@WebServlet("/customer")
public class AddCustomerController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("fname");
        String lastName = req.getParameter("lname");
        String dateOfBirth = req.getParameter("dbs");
        String mobileNumber = req.getParameter("mob");
        String email = req.getParameter("email");
        String gender = req.getParameter("gender");
        String branch = req.getParameter("branch");
        String password = req.getParameter("pass");
        String permanentAddress = req.getParameter("peradd");
        String presentAddress = req.getParameter("pradd");
        String message;

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "INSERT INTO customers(first_name, last_name, date_of_birth, mobile_number, email, gender, branch, password, permanent_address, present_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, dateOfBirth);
                ps.setString(4, mobileNumber);
                ps.setString(5, email);
                ps.setString(6, gender);
                ps.setString(7, branch);
                ps.setString(8, password);
                ps.setString(9, permanentAddress);
                ps.setString(10, presentAddress);

                int count = ps.executeUpdate();
                int customerId = 0;

                if (count > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            customerId = rs.getInt(1);
                        }
                    }
                    message = "Registration Successful! Your Customer ID is: " + customerId;
                } else {
                    message = "Something went wrong";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "<h3 style='color:red'>Exception Occurred: " + e.getMessage() + "</h3>";
        } finally {
            DatabaseUtil.closeConnection(con);
        }

        resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}
