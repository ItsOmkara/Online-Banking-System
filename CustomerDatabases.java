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

@WebServlet("/testAdminView")
public class CustomerDatabases extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        
        List<CustomerRegisters> customers = new ArrayList<>();

        
        System.out.println("Starting database connection...");

        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");
            System.out.println("Connected to the database successfully!");

            
            String query = "SELECT * FROM customers"; 
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            
            while (rs.next()) {
                CustomerRegisters customer = new CustomerRegisters(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("date_of_birth"),
                    rs.getString("mobile_number"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getString("branch"),
                    rs.getString("password"), 
                    rs.getString("permanent_address"),
                    rs.getString("present_address")
                );

                customers.add(customer);
                
                System.out.println("Fetched customer: " + customer.getFirstName() + " " + customer.getLastName());
                
            }

            con.close();
            System.out.println("Database connection closed.");

        } catch (Exception e) {
            System.out.println("Error fetching data!");
            e.printStackTrace();
        }

        
        Gson gson = new Gson();
        String json = gson.toJson(customers);

        
        System.out.println("Generated JSON: " + json);

        
        out.print(json);
        out.flush();
    }
}

