package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/BlockUnblockServlet")
public class BlockUnblockServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String action = request.getParameter("action"); // BLOCK / UNBLOCK

        String newStatus = action.equals("BLOCK") ? "BLOCKED" : "ACTIVE";

        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "UPDATE users SET status=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setString(2, username);
            ps.executeUpdate();

            ps.close();
            con.close();

            response.getWriter().print("success");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("error");
        }
    }
}
