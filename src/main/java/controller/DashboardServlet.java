package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        resp.setContentType("application/json");

        if (session == null || session.getAttribute("username") == null) {
            resp.getWriter().print("{\"status\":\"unauthorized\"}");
            return;
        }

        String username = session.getAttribute("username").toString();

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection (password = root)
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            /* ===== USER NAME (users table) ===== */
            PreparedStatement u = con.prepareStatement(
                "SELECT username FROM users WHERE username=?"
            );
            u.setString(1, username);
            ResultSet urs = u.executeQuery();

            String accountHolder = username; // fallback

            if (urs.next()) {
                accountHolder = urs.getString("username");
            }

            /* ===== ACCOUNT DATA (accounts table) ===== */
            PreparedStatement a = con.prepareStatement(
                "SELECT acc_no, father_name, account_type, balance FROM accounts WHERE username=?"
            );
            a.setString(1, username);
            ResultSet ars = a.executeQuery();

            if (ars.next()) {
                resp.getWriter().print(
                    "{"
                    + "\"status\":\"account_exists\","
                    + "\"accountHolder\":\"" + accountHolder + "\","
                    + "\"accNo\":\"" + ars.getString("acc_no") + "\","
                    + "\"fatherName\":\"" + ars.getString("father_name") + "\","
                    + "\"accountType\":\"" + ars.getString("account_type") + "\","
                    + "\"balance\":\"" + ars.getString("balance") + "\""
                    + "}"
                );
            } else {
                resp.getWriter().print(
                    "{"
                    + "\"status\":\"no_account\","
                    + "\"accountHolder\":\"" + accountHolder + "\""
                    + "}"
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print("{\"status\":\"error\"}");
        }
    }
}
