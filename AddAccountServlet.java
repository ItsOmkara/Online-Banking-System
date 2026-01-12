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

@WebServlet("/addAccount")
public class AddAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int customerId = Integer.parseInt(req.getParameter("customerId"));
        String accountType = req.getParameter("accountType");
        String cardNo = req.getParameter("cardNumber");
        String atmPin = req.getParameter("atmPin");
        String message;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Check if the customer exists
            String checkCustomerQuery = "SELECT customer_id FROM customers WHERE customer_id = ?";
            PreparedStatement checkCustomerPs = con.prepareStatement(checkCustomerQuery);
            checkCustomerPs.setInt(1, customerId);
            ResultSet rs = checkCustomerPs.executeQuery();

            if (!rs.next()) {
                // Customer does not exist
                message = "Customer ID does not exist!";
                System.out.println("Customer ID does not exist!");
            } else {
                // Check if the card number already exists
                String checkCardQuery = "SELECT cardno FROM accounts WHERE cardno = ?";
                PreparedStatement checkCardPs = con.prepareStatement(checkCardQuery);
                checkCardPs.setString(1, cardNo);
                ResultSet cardRs = checkCardPs.executeQuery();

                if (cardRs.next()) {
                    // Card number already exists
                    message = "Card number already exists!";
                    System.out.println("Card number already exists!");
                } else {
                    // Insert the new account
                    String insertQuery = "INSERT INTO accounts (customer_id, accounttype, cardno, atmpin, balance) VALUES (?, ?, ?, ?, 0.00)";
                    PreparedStatement insertPs = con.prepareStatement(insertQuery);
                    insertPs.setInt(1, customerId);
                    insertPs.setString(2, accountType);
                    insertPs.setString(3, cardNo);
                    insertPs.setString(4, atmPin);

                    int count = insertPs.executeUpdate();
                    if (count > 0) {
                        message = "Account added successfully!";
                        System.out.println("Account added successfully!");
                    } else {
                        message = "Failed to add account!";
                        System.out.println("Failed to add account!");
                    }

                    insertPs.close();
                }

                checkCardPs.close();
                cardRs.close();
            }

            // Close resources
            rs.close();
            checkCustomerPs.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred: " + e.getMessage();
        }

        // Redirect with the result message
        resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}