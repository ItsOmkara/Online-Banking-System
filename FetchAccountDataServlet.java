package LM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FetchAccountDataServlet")
public class FetchAccountDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Get the email from the request parameters
        String email = req.getParameter("email");
        System.out.println("Email received in FetchAccountDataServlet: " + email); // Debugging: Log the email

        // Object to hold the account data
        AccountData accountData = new AccountData();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Query to fetch account data based on email
            String query = "SELECT c.first_name, c.last_name, c.mobile_number, c.branch, a.cardno, a.atmpin, a.accounttype " +
                           "FROM customers c " +
                           "JOIN accounts a ON c.customer_id = a.customer_id " +
                           "WHERE c.email = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Populate the account data object
                accountData.setFirstName(rs.getString("first_name"));
                accountData.setLastName(rs.getString("last_name"));
                accountData.setMobileNumber(rs.getString("mobile_number"));
                accountData.setBranch(rs.getString("branch"));
                accountData.setCardNo(rs.getString("cardno"));
                accountData.setAtmPin(rs.getString("atmpin"));
                accountData.setAccountType(rs.getString("accounttype"));
            } else {
                // No data found
                accountData.setErrorMessage("No account data found for the given email.");
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            accountData.setErrorMessage("An error occurred while fetching account data.");
        }

        // Convert the account data object to JSON
        Gson gson = new Gson();
        String json = gson.toJson(accountData);

        // Send JSON response
        out.print(json);
        out.flush();
    }
}