package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/ViewUsersServlet")
public class ViewUsersServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sbi_bank";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "SELECT username, email, contact, status FROM users";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                        .append("\"username\":\"").append(rs.getString("username")).append("\",")
                        .append("\"email\":\"").append(rs.getString("email")).append("\",")
                        .append("\"contact\":\"").append(rs.getString("contact")).append("\",")
                        .append("\"status\":\"").append(rs.getString("status")).append("\"")
                        .append("}");
            }

            json.append("]");
            out.print(json.toString());

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
}
