package LM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fetchTransactions")
public class FetchTransactionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // List to hold all transaction records
        List<TransactionCustomer> transactions = new ArrayList<>();

        // Add console logs for debugging
        System.out.println("Starting database connection...");

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");
            System.out.println("Connected to the database successfully!");

            // Query to fetch joined data from transactions and customers tables
            String query = "SELECT t.transaction_id, t.cardno, t.amount, t.transaction_type, t.transaction_date, " +
                           "c.first_name, c.last_name, c.mobile_number, c.email " +
                           "FROM transactions t " +
                           "INNER JOIN accounts a ON t.cardno = a.cardno " +
                           "INNER JOIN customers c ON a.customer_id = c.customer_id";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Populate transaction list from ResultSet
            while (rs.next()) {
                TransactionCustomer tc = new TransactionCustomer();
                tc.setTransactionId(rs.getInt("transaction_id"));
                tc.setCardNo(rs.getString("cardno"));
                tc.setAmount(rs.getDouble("amount"));
                tc.setTransactionType(rs.getString("transaction_type"));
                tc.setTransactionDate(rs.getString("transaction_date"));
                tc.setFirstName(rs.getString("first_name"));
                tc.setLastName(rs.getString("last_name"));
                tc.setMobileNumber(rs.getString("mobile_number"));
                tc.setEmail(rs.getString("email"));

                transactions.add(tc);

                System.out.println("Fetched transaction: " + tc.getTransactionId() + " for card: " + tc.getCardNo());
            }

            con.close();
            System.out.println("Database connection closed.");

        } catch (Exception e) {
            System.out.println("Error fetching data!");
            e.printStackTrace();
        }

        // Convert the transaction list to JSON
        Gson gson = new Gson();
        String json = gson.toJson(transactions);

        System.out.println("Generated JSON: " + json);

        // Send JSON response
        out.print(json);
        out.flush();
    }
}

