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

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderAtmPin = req.getParameter("atmPin"); // Sender's ATM PIN
        String recipientCardNo = req.getParameter("accountNumber"); // Recipient's Card Number
        double amount = Double.parseDouble(req.getParameter("amount")); // Amount to transfer
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
        PreparedStatement senderPs = null;
        ResultSet senderRs = null;
        PreparedStatement recipientPs = null;
        ResultSet recipientRs = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Step 1: Fetch sender details
            String senderQuery = "SELECT a.balance, a.cardno FROM accounts a " +
                                 "JOIN customers c ON a.customer_id = c.customer_id " +
                                 "WHERE a.atmpin = ? AND c.email = ?";
            senderPs = con.prepareStatement(senderQuery);
            senderPs.setString(1, senderAtmPin);
            senderPs.setString(2, loggedInEmail);
            senderRs = senderPs.executeQuery();

            if (senderRs.next()) {
                double senderBalance = senderRs.getDouble("balance");
                String senderCardNo = senderRs.getString("cardno");

                System.out.println("Sender balance: " + senderBalance); // Debug log
                System.out.println("Sender cardno: " + senderCardNo); // Debug log

                // Step 2: Check sender's balance
                if (senderBalance >= amount) {
                    // Step 3: Verify recipient exists
                    String recipientQuery = "SELECT cardno FROM accounts WHERE cardno = ?";
                    recipientPs = con.prepareStatement(recipientQuery);
                    recipientPs.setString(1, recipientCardNo);
                    recipientRs = recipientPs.executeQuery();

                    if (recipientRs.next()) {
                        System.out.println("Recipient found: " + recipientCardNo); // Debug log

                        // Step 4: Deduct amount from sender
                        String deductQuery = "UPDATE accounts SET balance = balance - ? WHERE cardno = ?";
                        try (PreparedStatement deductPs = con.prepareStatement(deductQuery)) {
                            deductPs.setDouble(1, amount);
                            deductPs.setString(2, senderCardNo);
                            int rowsUpdated = deductPs.executeUpdate();
                            System.out.println("Rows updated (deduct): " + rowsUpdated); // Debug log
                        }

                        // Step 5: Add amount to recipient
                        String addQuery = "UPDATE accounts SET balance = balance + ? WHERE cardno = ?";
                        try (PreparedStatement addPs = con.prepareStatement(addQuery)) {
                            addPs.setDouble(1, amount);
                            addPs.setString(2, recipientCardNo);
                            int rowsUpdated = addPs.executeUpdate();
                            System.out.println("Rows updated (add): " + rowsUpdated); // Debug log
                        }

                        // Step 6: Insert transaction record for sender (Transfer Out)
                        String senderTransactionQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                        try (PreparedStatement senderTransactionPs = con.prepareStatement(senderTransactionQuery)) {
                            senderTransactionPs.setString(1, senderCardNo);
                            senderTransactionPs.setDouble(2, amount);
                            senderTransactionPs.setString(3, "Transfer Out");
                            senderTransactionPs.executeUpdate();
                            System.out.println("Transaction record inserted for sender."); // Debug log
                        }

                        // Step 7: Insert transaction record for recipient (Transfer In)
                        String recipientTransactionQuery = "INSERT INTO transactions (cardno, amount, transaction_type) VALUES (?, ?, ?)";
                        try (PreparedStatement recipientTransactionPs = con.prepareStatement(recipientTransactionQuery)) {
                            recipientTransactionPs.setString(1, recipientCardNo);
                            recipientTransactionPs.setDouble(2, amount);
                            recipientTransactionPs.setString(3, "Transfer In");
                            recipientTransactionPs.executeUpdate();
                            System.out.println("Transaction record inserted for recipient."); // Debug log
                        }

                        message = "Transfer successful!";
                        System.out.println("Transfer successful from cardno: " + senderCardNo + " to cardno: " + recipientCardNo);
                    } else {
                        message = "Recipient's account not found!";
                        System.out.println("Recipient's account not found: " + recipientCardNo);
                    }
                } else {
                    message = "Insufficient balance!";
                    System.out.println("Insufficient balance for cardno: " + senderCardNo);
                }
            } else {
                message = "Invalid ATM pin!";
                System.out.println("Invalid ATM pin: " + senderAtmPin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred. Please try again.";
        } finally {
            // Close resources
            if (senderRs != null) {
                try {
                    senderRs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (senderPs != null) {
                try {
                    senderPs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (recipientRs != null) {
                try {
                    recipientRs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (recipientPs != null) {
                try {
                    recipientPs.close();
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