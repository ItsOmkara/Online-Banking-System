package LM10;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Loginform")
public class Loginform extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String message;

        // Validate email format
        if (!isValidEmail(email)) {
            message = "Invalid email format. Please use a valid Gmail address.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }

        // Validate password (e.g., minimum length, special characters, etc.)
        if (!isValidPassword(password)) {
            message = "Invalid password. Password must be at least 8 characters long and contain at least one special character.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return;
        }

        // Admin login check
        if ("admin@gmail.com".equals(email) && "Admin@123".equals(password)) {
            // Create a session for the admin
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("role", "admin"); // Store the role as "admin"

            // Redirect to admins.html for the specific admin user
            message = "Admin Login Successful!";
            resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            return; // Exit the method to prevent further processing
        }

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            // Prepare SQL query to check email and password
            String query = "SELECT first_name FROM customers WHERE email = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Login successful
                String firstName = rs.getString("first_name");

                // Create a session for the user
                HttpSession session = req.getSession();
                session.setAttribute("email", email);
                session.setAttribute("firstName",firstName);

                message = "Login Successful!";
                String redirectURL = "customeracc.html?first_name=" + java.net.URLEncoder.encode(firstName, "UTF-8") +
                        "&email=" + java.net.URLEncoder.encode(email, "UTF-8") +
                        "&message=" + java.net.URLEncoder.encode(message, "UTF-8");
                resp.sendRedirect(redirectURL);
                System.out.println("Login successful for: " + firstName);
            } else {
                // Login failed
                message = "Invalid email or password";
                resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                System.out.println("Login failed for: " + email);
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to home with error message
            message = "An error occurred. Please try again.";
            resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:gmail\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Method to validate password
    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least one special character
        String passwordRegex = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }
}