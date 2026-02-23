package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

public class CheckBalanceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");
        long accNo = Long.parseLong(req.getParameter("accNo"));

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection (Apna database name yaha likhna)
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE acc_no=? AND username=?"
            );
            ps.setLong(1, accNo);
            ps.setString(2, username);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                con.close();
                resp.sendRedirect("balance.html?status=invalid");
                return;
            }

            double balance = rs.getDouble("balance");
            con.close();

            resp.sendRedirect(
                "balance.html?status=success&balance=" + balance
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("balance.html?status=error");
        }
    }
}
