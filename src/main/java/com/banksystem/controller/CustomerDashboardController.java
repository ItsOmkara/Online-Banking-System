package com.banksystem.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for Customer Dashboard access.
 */
@WebServlet("/CustomerDashboard")
public class CustomerDashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("email") != null && session.getAttribute("role") == null) {
            req.getRequestDispatcher("Customers.html").forward(req, resp);
        } else {
            String message = "Unauthorized access. Please login first.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }
}
