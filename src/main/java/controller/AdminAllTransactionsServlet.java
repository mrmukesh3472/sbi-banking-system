package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/AdminAllTransactionsServlet")
public class AdminAllTransactionsServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql =
                "SELECT id, username, trans_type, amount, acc_no, related_acc, trans_date " +
                "FROM transactions ORDER BY trans_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                    .append("\"id\":").append(rs.getLong("id")).append(",")
                    .append("\"username\":\"").append(rs.getString("username")).append("\",")
                    .append("\"type\":\"").append(rs.getString("trans_type")).append("\",")
                    .append("\"amount\":").append(rs.getDouble("amount")).append(",")
                    .append("\"accNo\":").append(rs.getLong("acc_no")).append(",")
                    .append("\"relatedAcc\":").append(rs.getLong("related_acc")).append(",")
                    .append("\"date\":\"").append(rs.getTimestamp("trans_date")).append("\"")
                    .append("}");
            }

            json.append("]");
            out.print(json.toString());

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
}
