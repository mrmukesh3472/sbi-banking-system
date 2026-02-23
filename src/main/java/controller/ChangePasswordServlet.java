package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class ChangePasswordServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html?status=loginrequired");
            return;
        }

        String username = (String) session.getAttribute("username");
        String oldPass = req.getParameter("oldPassword");
        String newPass = req.getParameter("newPassword");
        String confirmPass = req.getParameter("confirmPassword");

        if (oldPass == null || newPass == null || confirmPass == null) {
            resp.sendRedirect("changepassword.html?status=error");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            resp.sendRedirect("changepassword.html?status=mismatch");
            return;
        }

        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASS
            );

            // ✅ Old password verify
            PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM users WHERE username=? AND password=?"
            );
            ps.setString(1, username);
            ps.setString(2, oldPass);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                resp.sendRedirect("changepassword.html?status=wrongold");
                return;
            }

            // ✅ Update password
            PreparedStatement ps2 = con.prepareStatement(
                "UPDATE users SET password=? WHERE username=?"
            );
            ps2.setString(1, newPass);
            ps2.setString(2, username);

            int updated = ps2.executeUpdate();

            if (updated > 0) {
                resp.sendRedirect("changepassword.html?status=success");
            } else {
                resp.sendRedirect("changepassword.html?status=error");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("changepassword.html?status=error");
        }
    }
}
