package LM10;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CheckBalanceServlet")
public class CheckBalanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String atmPin = req.getParameter("atmPin");

        
        double balance = 0.0;
        String message = "An error occurred. Please try again."; // Default error message

        
        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");
        String firstName = (String) session.getAttribute("firstName");

        if (loggedInEmail == null || firstName == null) {
            
            resp.sendRedirect("home.html");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Query to fetch balance based on ATM PIN and logged-in email
            String query = "SELECT a.balance FROM accounts a " +
                           "JOIN customers c ON a.customer_id = c.customer_id " +
                           "WHERE c.email = ? AND a.atmpin = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ps.setString(2, atmPin);
            rs = ps.executeQuery();

            // Check if a record was found
            if (rs.next()) {
                balance = rs.getDouble("balance");
                message = "Balance fetched successfully!";
            } else {
                message = "Invalid ATM pin!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred. Please try again.";
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Redirect back to the customeracc.html page with balance and user info
        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                             "&email=" + java.net.URLEncoder.encode(loggedInEmail, "UTF-8") +
                             "&balance=" + balance +
                             "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }

}
