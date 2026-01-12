package com.banksystem.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for Admin Dashboard access.
 */
@WebServlet("/AdminDashboard")
public class AdminDashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null && "admin".equals(session.getAttribute("role"))) {
            req.getRequestDispatcher("/WEB-INF/Admins.html").forward(req, resp);
        } else {
            String message = "Unauthorized access. Please login as admin.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }
}
