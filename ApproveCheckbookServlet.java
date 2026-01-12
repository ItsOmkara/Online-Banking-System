package LM10;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/approveCheckbook")
public class ApproveCheckbookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            // Read JSON input
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            int accountId = jsonObject.get("accountId").getAsInt();

            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Update checkbook_status to 'Approved'
            String query = "UPDATE accounts SET checkbook_status = 'Approved' WHERE account_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accountId);
            int rowsUpdated = ps.executeUpdate();

            con.close();

            // Send response
            if (rowsUpdated > 0) {
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false}");
            }
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false}");
            out.flush();
        }
    }
}
