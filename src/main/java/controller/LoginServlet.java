package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class LoginServlet extends HttpServlet {

    // ===== FIXED ADMIN USERS =====
    private static final List<String> ADMIN_USERS =
            Arrays.asList("admin", "manager", "superadmin");

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userType = req.getParameter("userType"); // User / Admin
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASS
            );

            PreparedStatement ps = con.prepareStatement(
                "SELECT status FROM users WHERE TRIM(username)=TRIM(?) AND TRIM(password)=TRIM(?)"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // ================= ADMIN LOGIN =================
                if ("Admin".equalsIgnoreCase(userType)) {

                    if (!ADMIN_USERS.contains(username.toLowerCase())) {
                        resp.sendRedirect("login.html?status=unauthorized");
                        return;
                    }

                    HttpSession session = req.getSession(true);
                    session.setAttribute("adminUsername", username);

                    // ✅ ADMIN SUCCESS STATUS
                    resp.sendRedirect("login.html?status=adminsuccess");
                }

                // ================= USER LOGIN =================
                else {

                    String status = rs.getString("status");

                    if ("BLOCKED".equalsIgnoreCase(status)) {
                        resp.sendRedirect("login.html?blocked=true");
                        return;
                    }

                    HttpSession session = req.getSession(true);
                    session.setAttribute("username", username);

                    resp.sendRedirect("login.html?status=success");
                }

            } else {
                resp.sendRedirect("login.html?status=invalid");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("login.html?status=error");
        }
    }
}
