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

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String atmPin = req.getParameter("atmPin"); // Get ATM pin from the form
        double amount = Double.parseDouble(req.getParameter("amount")); // Get amount from the form
        String message = "An error occurred. Please try again."; // Default error message

        // Get the logged-in user's email and first name from the session
        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");
        String firstName = (String) session.getAttribute("firstName");

        if (loggedInEmail == null || firstName == null) {
            // If no session, redirect to login page
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

            // Fetch card number using the logged-in email and ATM pin
            String query = "SELECT a.cardno FROM accounts a " +
                           "JOIN customers c ON a.customer_id = c.customer_id WHERE c.email = ? AND a.atmpin = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ps.setString(2, atmPin);
            rs = ps.executeQuery();

            if (rs.next()) {
                String cardno = rs.getString("cardno");

                // Update balance
                String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE atmpin = ?";
                try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                    updatePs.setDouble(1, amount);
                    updatePs.setString(2, atmPin);
                    
                    int rowsUpdated = updatePs.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Insert transaction record
                        String insertQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                        try (PreparedStatement insertPs = con.prepareStatement(insertQuery)) {
                            insertPs.setString(1, cardno);
                            insertPs.setDouble(2, amount);
                            insertPs.setString(3, "Deposit");
                            insertPs.executeUpdate();
                        }
                        
                        message = "Deposit successful!";
                        System.out.println("Deposit successful for ATM pin: " + atmPin);
                    } else {
                        message = "Failed to deposit amount!";
                        System.out.println("Failed to deposit amount for ATM pin: " + atmPin);
                    }
                }
            } else {
                message = "Invalid ATM pin!";
                System.out.println("Invalid ATM pin: " + atmPin);
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

        // Redirect with details
        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                "&email=" + java.net.URLEncoder.encode(loggedInEmail, "UTF-8") +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        resp.sendRedirect(redirectURL);
    }
}