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
import jakarta.servlet.http.HttpSession;

import com.banksystem.util.DatabaseUtil;
import com.banksystem.util.ValidationUtil;

/**
 * Controller for user login functionality.
 */
@WebServlet("/Loginform")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String message;

        // Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            message = "Invalid email format. Please use a valid Gmail address.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }

        // Validate password
        if (!ValidationUtil.isValidPassword(password)) {
            message = "Invalid password. Password must be at least 8 characters long and contain at least one special character.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }

        // Admin login check
        if ("admin@gmail.com".equals(email) && "Admin@123".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("role", "admin");
            message = "Admin Login Successful!";
            resp.sendRedirect("AdminDashboard?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DatabaseUtil.getConnection();

            String query = "SELECT first_name FROM customers WHERE email = ? AND password = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                HttpSession session = req.getSession();
                session.setAttribute("email", email);
                session.setAttribute("firstName", firstName);
                message = "Login Successful!";
                resp.sendRedirect("CustomerDashboard");
                System.out.println("Login successful for: " + firstName);
            } else {
                message = "Invalid email or password";
                resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                System.out.println("Login failed for: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred. Please try again.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseUtil.closeConnection(con);
        }
    }
}
