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
@WebServlet("/fetchAccounts")
public class FetchAccountsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<AccountCustomer> accountCustomers = new ArrayList<>();

        System.out.println("Starting database connection...");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");
            System.out.println("Connected to the database successfully!");

            // **Updated Query to Include checkbook_status**
            String query = "SELECT a.account_id, a.customer_id, a.cardno, a.atmpin, a.accounttype, a.balance, " +
                           "a.checkbook_status, c.first_name, c.last_name, c.mobile_number, c.email " +
                           "FROM accounts a " +
                           "INNER JOIN customers c ON a.customer_id = c.customer_id";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AccountCustomer ac = new AccountCustomer();
                ac.setAccountId(rs.getInt("account_id"));
                ac.setCustomerId(rs.getInt("customer_id"));
                ac.setCardNo(rs.getString("cardno"));
                ac.setAtmPin(rs.getString("atmpin"));
                ac.setAccountType(rs.getString("accounttype"));
                ac.setBalance(rs.getDouble("balance"));
                ac.setFirstName(rs.getString("first_name"));
                ac.setLastName(rs.getString("last_name"));
                ac.setMobileNumber(rs.getString("mobile_number"));
                ac.setEmail(rs.getString("email"));
                ac.setCheckbookStatus(rs.getString("checkbook_status")); // **Set Checkbook Status**

                accountCustomers.add(ac);
                System.out.println("Fetched account: " + ac.getAccountId() + " | Checkbook Status: " + ac.getCheckbookStatus());
            }

            con.close();
            System.out.println("Database connection closed.");

        } catch (Exception e) {
            System.out.println("Error fetching data!");
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String json = gson.toJson(accountCustomers);

        System.out.println("Generated JSON: " + json);

        out.print(json);
        out.flush();
    }
}
