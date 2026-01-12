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

@WebServlet("/customer")
public class Customerform extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String firstName = req.getParameter("fname");
        String lastName = req.getParameter("lname");
        String dateofBirth = req.getParameter("dbs");
        String mobileNumber = req.getParameter("mob");
        String email = req.getParameter("email");
        String gender = req.getParameter("gender");
        String branch = req.getParameter("branch");
        String password = req.getParameter("pass");
        String Paddress = req.getParameter("peradd");
        String PrAddress = req.getParameter("pradd");
        String message;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Insert query with RETURN_GENERATED_KEYS to retrieve the auto-generated customer_id
            String query = "INSERT INTO customers(first_name, last_name, date_of_birth, mobile_number, email, gender, branch, password, permanent_address, present_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, dateofBirth);
            ps.setString(4, mobileNumber);
            ps.setString(5, email);
            ps.setString(6, gender);
            ps.setString(7, branch);
            ps.setString(8, password);
            ps.setString(9, Paddress);
            ps.setString(10, PrAddress);

            int count = ps.executeUpdate();

            // Retrieve the auto-generated customer_id
            int customerId = 0;
            if (count > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    customerId = rs.getInt(1); // Get the first column (customer_id)
                }
            }

            System.out.println("First Name: " + req.getParameter("fname"));
            System.out.println("Last Name: " + req.getParameter("lname"));
            System.out.println("Date of Birth: " + req.getParameter("dbs"));
            System.out.println("Mobile Number: " + req.getParameter("mob"));
            System.out.println("Email: " + req.getParameter("email"));
            System.out.println("Gender: " + req.getParameter("gender"));
            System.out.println("Branch: " + req.getParameter("branch"));
            System.out.println("Password: " + req.getParameter("pass"));
            System.out.println("Permanent Address: " + req.getParameter("peradd"));
            System.out.println("Present Address: " + req.getParameter("pradd"));

            if (req.getParameter("fname") == null || req.getParameter("fname").isEmpty()) {
                System.out.println("ERROR: First name is NULL or empty.");
            }

            if (count > 0) {
                message = "Registration Successful! Your Customer ID is: " + customerId; // Include customer_id in the message
                System.out.println("Registration Successful! Customer ID: " + customerId);
            } else {
                message = "Something went wrong"; // Message for failed registration
                System.out.println("Registration Unsuccessful");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            message = ("<h3 style='color:red'>Exception Occurred: " + e.getMessage() + "</h3>");
        }

        // Redirect with the message
        resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}