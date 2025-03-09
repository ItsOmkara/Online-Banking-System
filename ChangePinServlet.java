package LM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/ChangePinServlet")
public class ChangePinServlet extends HttpServlet {
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
        ChangePinRequest changePinRequest = gson.fromJson(requestBody.toString(), ChangePinRequest.class);

        String cardNo = changePinRequest.getCardNo();
        String oldPin = changePinRequest.getOldPin();
        String newPin = changePinRequest.getNewPin();

        String message;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Check if the old PIN is correct
            String checkPinQuery = "SELECT * FROM accounts WHERE cardno = ? AND atmpin = ?";
            PreparedStatement checkPinStmt = con.prepareStatement(checkPinQuery);
            checkPinStmt.setString(1, cardNo);
            checkPinStmt.setString(2, oldPin);
            ResultSet rs = checkPinStmt.executeQuery();

            if (rs.next()) {
                // Old PIN is correct, update the PIN
                String updatePinQuery = "UPDATE accounts SET atmpin = ? WHERE cardno = ?";
                PreparedStatement updatePinStmt = con.prepareStatement(updatePinQuery);
                updatePinStmt.setString(1, newPin);
                updatePinStmt.setString(2, cardNo);

                int rowsUpdated = updatePinStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    message = "PIN changed successfully!";
                } else {
                    message = "Failed to change PIN.";
                }
            } else {
                // Old PIN is incorrect
                message = "Incorrect old PIN.";
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred while changing the PIN.";
        }

        // Send the response
        ChangePinResponse response = new ChangePinResponse(message);
        out.print(gson.toJson(response));
        out.flush();
    }
}
