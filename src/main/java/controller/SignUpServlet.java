package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");
        String contact  = req.getParameter("contact");

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            String sql = "INSERT INTO users (username, password, email, contact) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, contact);

            ps.executeUpdate();
            con.close();

            // ✅ SUCCESS redirect
            resp.sendRedirect("signup.html?status=success");

        } catch (Exception e) {

            // ❌ ERROR redirect
            resp.sendRedirect("signup.html?status=error");
        }
    }
}
