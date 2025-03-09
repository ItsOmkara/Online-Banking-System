package LM10;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/TransactionHistoryServlet")
public class TransactionHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the logged-in user's email from the session
        HttpSession session = req.getSession();
        String loggedInEmail = (String) session.getAttribute("email");

        if (loggedInEmail == null) {
            // If no session, redirect to login page
            resp.sendRedirect("home.html");
            return;
        }

        List<Transaction> transactions = new ArrayList<>();

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Fetch transaction history for the logged-in user
            String query = "SELECT t.transaction_id, t.amount, t.cardno, t.transaction_type, t.transaction_date " +
                           "FROM transactions t " +
                           "JOIN accounts a ON t.cardno = a.cardno " +
                           "JOIN customers c ON a.customer_id = c.customer_id " +
                           "WHERE c.email = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getDouble("amount"),
                    rs.getString("cardno"),
                    rs.getString("transaction_type"),
                    rs.getTimestamp("transaction_date").toString()
                );
                transactions.add(transaction);
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert transactions list to JSON
        String json = new Gson().toJson(transactions);

        // Set response content type to JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Send JSON response
        resp.getWriter().write(json);
    }
}

