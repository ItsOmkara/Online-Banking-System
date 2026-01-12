package LM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/applyForCheckbook")
public class ApplyForCheckbookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Parse the request body
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            requestBody.append(line);
        }

        // Convert the request body to JSON
        Gson gson = new Gson();
        ApplyForCheckbookRequest request = gson.fromJson(requestBody.toString(), ApplyForCheckbookRequest.class);

        String cardNo = request.getCardNo();
        String message;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Update the checkbook status to "Pending"
            String query = "UPDATE accounts SET checkbook_status = 'Pending' WHERE cardno = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, cardNo);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                message = "Checkbook application submitted successfully!";
            } else {
                message = "Failed to submit checkbook application.";
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred while submitting the checkbook application.";
        }

        // Send the response
        ApplyForCheckbookResponse response = new ApplyForCheckbookResponse(message);
        out.print(gson.toJson(response));
        out.flush();
    }
}

