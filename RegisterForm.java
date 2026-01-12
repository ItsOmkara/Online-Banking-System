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

@WebServlet("/Registerform")
public class RegisterForm extends HttpServlet {
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
		
		PrintWriter out = resp.getWriter();
		String name = req.getParameter("fullname");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String confirmpass = req.getParameter("confirmpassword");
		String message;
		try
		{
			
			if (password != null && confirmpass != null && password.trim().equals(confirmpass.trim())) {
			    // Passwords match, proceed
			} else {
			    message = "Passwords do not match!";
			    resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
			    return; // Exit early
			} 
			
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankSystem","root","sharada");
			PreparedStatement ps = con .prepareStatement("insert into registerform (fullname, email, password, confirmpass) values(?,?,?,?)");
			ps.setString(1,name);
			ps.setString(2,email);
			ps.setString(3,password);
			ps.setString(4,confirmpass);
			int count = ps.executeUpdate();
			if (count>0) {
                // Login successful
                message = "Registeration Successful!"; // Message for successful login
                System.out.println("Registeration Successfully");
                
            } else {
                // Login failed
                message = "Something went wrong"; // Message for failed login
                System.out.println("Registeration Unsuccessful");
                
            }
           

            // Close resources
            
            ps.close();
            con.close();

}
		catch(Exception e) {
           e.printStackTrace();
			message = ("<h3 style = 'color : red'>Exception Occured: "+e.getMessage()+"</h3>");
		}
		resp.sendRedirect("home.html?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
}
}
