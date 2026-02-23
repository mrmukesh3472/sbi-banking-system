package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class NewAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 🔐 SESSION CHECK
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");

        // FORM DATA
        String aadhar = req.getParameter("aadhar");
        String email = req.getParameter("email");
        String mobile = req.getParameter("mobile");
        String father = req.getParameter("father");
        String accountType = req.getParameter("accountType");
        String gender = req.getParameter("gender");

        double balance;
        try {
            balance = Double.parseDouble(req.getParameter("balance"));
        } catch (Exception e) {
            resp.sendRedirect("newaccount.html?status=error");
            return;
        }

        long accNo = 1000000000L + (long) (Math.random() * 9000000000L);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            /* ---------------------------------------------
               1️⃣ USER ALREADY HAS ACCOUNT ?
            --------------------------------------------- */
            ps = con.prepareStatement(
                "SELECT acc_no FROM accounts WHERE username=?"
            );
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                long existingAccNo = rs.getLong("acc_no");
                resp.sendRedirect(
                    "newaccount.html?status=already_created&accNo=" + existingAccNo
                );
                return;
            }

            rs.close();
            ps.close();

            /* ---------------------------------------------
               2️⃣ AADHAR ALREADY EXISTS ?
            --------------------------------------------- */
            ps = con.prepareStatement(
                "SELECT acc_no FROM accounts WHERE aadhar=?"
            );
            ps.setString(1, aadhar);
            rs = ps.executeQuery();

            if (rs.next()) {
                long existingAccNo = rs.getLong("acc_no");
                resp.sendRedirect(
                    "newaccount.html?status=aadhar_exists&accNo=" + existingAccNo
                );
                return;
            }

            rs.close();
            ps.close();

            /* ---------------------------------------------
               3️⃣ INSERT NEW ACCOUNT
            --------------------------------------------- */
            ps = con.prepareStatement(
                "INSERT INTO accounts VALUES (?,?,?,?,?,?,?,?,?)"
            );

            ps.setLong(1, accNo);
            ps.setString(2, username);
            ps.setString(3, aadhar);
            ps.setString(4, email);
            ps.setString(5, mobile);
            ps.setString(6, father);
            ps.setString(7, accountType);
            ps.setDouble(8, balance);
            ps.setString(9, gender);

            ps.executeUpdate();

            resp.sendRedirect(
                "newaccount.html?status=success&accNo=" + accNo
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("newaccount.html?status=error");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }
}
