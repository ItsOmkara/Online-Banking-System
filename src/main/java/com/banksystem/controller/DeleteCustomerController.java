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
 * Controller for deleting customers.
 */
@WebServlet("/deleteCustomer")
public class DeleteCustomerController extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("id");

        if (customerId == null || customerId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Customer ID is required.");
            return;
        }

        Connection con = null;

        try {
            con = DatabaseUtil.getConnection();

            String sql = "DELETE FROM customers WHERE customer_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(customerId));

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Customer deleted successfully.");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Customer not found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while deleting the customer.");
        } finally {
            DatabaseUtil.closeConnection(con);
        }
    }
}
