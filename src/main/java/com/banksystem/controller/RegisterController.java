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
 * Controller for user registration.
 */
@WebServlet("/Registerform")
public class RegisterController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmpass = req.getParameter("confirmpassword");
        String message;

        try {
            if (password != null && confirmpass != null && password.trim().equals(confirmpass.trim())) {
                // Passwords match, proceed
            } else {
                message = "Passwords do not match!";
                resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                return;
            }

            Connection con = DatabaseUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO registerform (fullname, email, password, confirmpass) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, confirmpass);

            int count = ps.executeUpdate();
            if (count > 0) {
                message = "Registration Successful!";
                System.out.println("Registration Successful");
            } else {
                message = "Something went wrong";
                System.out.println("Registration Unsuccessful");
            }

            ps.close();
            DatabaseUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
            message = "<h3 style='color:red'>Exception Occurred: " + e.getMessage() + "</h3>";
        }

        resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}
