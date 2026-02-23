package controller;

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

@WebServlet("/admin/dashboard-data")
public class AdminDashboardServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int totalUsers = 0;

        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASS);

            PreparedStatement ps =
                    con.prepareStatement("SELECT COUNT(*) FROM users");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalUsers = rs.getInt(1);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // JSON response
        out.print("{\"totalUsers\": " + totalUsers + "}");
        out.flush();
    }
}
