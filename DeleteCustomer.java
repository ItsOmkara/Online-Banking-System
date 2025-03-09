package LM10;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/deleteCustomer")
public class DeleteCustomer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("id"); // Get the customer ID from the query parameter

        if (customerId == null || customerId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Send 400 Bad Request if ID is missing
            response.getWriter().write("Customer ID is required.");
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // SQL query to delete the customer by ID
            String sql = "DELETE FROM customers WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(customerId));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK); // Send 200 OK if deletion is successful
                response.getWriter().write("Customer deleted successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Send 404 if no customer was found
                response.getWriter().write("Customer not found.");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Send 500 Internal Server Error
            response.getWriter().write("An error occurred while deleting the customer.");
        }
    }
}
