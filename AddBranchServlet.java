package LM10;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addBranch")
public class AddBranchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String branchName = req.getParameter("branchName");
        String branchCode = req.getParameter("branchCode");
        String branchAddress = req.getParameter("branchAddress");
        String message;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem", "root", "sharada");

            String query = "INSERT INTO branches (branch_name, branch_code, branch_address) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, branchName);
            ps.setString(2, branchCode);
            ps.setString(3, branchAddress);
            int count = ps.executeUpdate();
            if(count>0) {
            	message = "Branch added successfully!";
                System.out.println("Branch added successfully!");
            } else {
                message = "Failed to add Branch!";
                System.out.println("Failed to add Branch!");
            }        
        }
        catch(Exception e) {
        	 e.printStackTrace();
             message = "An error occurred: " + e.getMessage();   	
        }
        resp.sendRedirect("Admins.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}
