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

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String atmPin = req.getParameter("atmPin"); // Get ATM pin from the form
        double amount = Double.parseDouble(req.getParameter("amount")); // Get amount from the form
        String message = "An error occurred. Please try again."; // Default error message
        String firstName = "", email = "";

        // Get the logged-in user's email from the session
        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");

        System.out.println("Session email retrieved: " + loggedInEmail); // Debug log

        if (loggedInEmail == null) {
            // If no session, redirect to login page
            resp.sendRedirect("home.html");
            return;
        }

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Fetch customer details using the logged-in email
            String customerQuery = "SELECT c.first_name, c.email " +
                                   "FROM customers c " +
                                   "JOIN accounts a ON c.customer_id = a.customer_id " +
                                   "WHERE c.email = ?";
            PreparedStatement customerPs = con.prepareStatement(customerQuery);
            customerPs.setString(1, loggedInEmail);
            ResultSet customerRs = customerPs.executeQuery();

            if (customerRs.next()) {
                firstName = customerRs.getString("first_name");
                email = customerRs.getString("email");
            } else {
                // If no customer found, set default values
                firstName = "Unknown";
                email = "unknown@example.com";
            }

            // Fetch balance and card number using the ATM pin
            String query = "SELECT a.balance, a.cardno " +
                           "FROM accounts a " +
                           "WHERE a.atmpin = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, atmPin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                String cardno = rs.getString("cardno");

                if (currentBalance >= amount) {
                    // Update balance
                    String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE atmpin = ?";
                    PreparedStatement updatePs = con.prepareStatement(updateQuery);
                    updatePs.setDouble(1, amount);
                    updatePs.setString(2, atmPin);
                    
                    int rowsUpdated = updatePs.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Insert transaction record
                        String insertQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, cardno);
                        insertPs.setDouble(2, amount);
                        insertPs.setString(3, "Withdraw");
                        insertPs.executeUpdate();
                        
                        message = "Withdrawal successful!";
                        System.out.println("Withdrawal successful for ATM pin: " + atmPin);
                    } else {
                        message = "Failed to withdraw amount!";
                        System.out.println("Failed to withdraw amount for ATM pin: " + atmPin);
                    }
                    updatePs.close();
                } else {
                    message = "Insufficient balance!";
                    System.out.println("Insufficient balance for ATM pin: " + atmPin);
                }
            } else {
                message = "Invalid ATM pin!";
                System.out.println("Invalid ATM pin: " + atmPin);
            }

            // Close resources
            rs.close();
            ps.close();
            customerRs.close();
            customerPs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect with details
        String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                "&email=" + java.net.URLEncoder.encode(email, "UTF-8") +
                "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
        System.out.println("Redirect URL: " + redirectURL); // Debug log
        resp.sendRedirect(redirectURL);
    }
}