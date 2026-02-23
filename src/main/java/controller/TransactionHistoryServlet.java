package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class TransactionHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            PreparedStatement ps = con.prepareStatement(
                "SELECT trans_date, trans_type, acc_no, amount, related_acc " +
                "FROM transactions WHERE username=? ORDER BY trans_date DESC"
            );
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                String type = rs.getString("trans_type");
                String cssClass = type.toLowerCase();

                out.println("<tr class='" + cssClass + "'>");
                out.println("<td>" + rs.getDate("trans_date") + "</td>");
                out.println("<td>" + type + "</td>");
                out.println("<td>" + rs.getLong("acc_no") + "</td>");
                out.println("<td>₹ " + rs.getDouble("amount") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='4'>No transactions found</td></tr>");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='4'>Error loading data</td></tr>");
        }
    }
}
