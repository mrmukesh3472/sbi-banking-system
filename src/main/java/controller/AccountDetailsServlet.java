package controller;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;

//@WebServlet("/AccountDetailsServlet")
public class AccountDetailsServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/sbi_bank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String accNo = req.getParameter("accNo");

        if (accNo == null || accNo.isEmpty()) {
            out.print("{\"error\":\"Account number required\"}");
            return;
        }

        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM accounts WHERE acc_no = ?"
            );

            ps.setLong(1, Long.parseLong(accNo));

            ResultSet rs = ps.executeQuery();
           
            if (rs.next()) {
                out.print("{");
                out.print("\"acc_no\":\"" + rs.getLong("acc_no") + "\",");
                out.print("\"username\":\"" + rs.getString("username") + "\",");
                out.print("\"father_name\":\"" + rs.getString("father_name") + "\",");
                out.print("\"email\":\"" + rs.getString("email") + "\",");
                out.print("\"mobile\":\"" + rs.getString("mobile") + "\",");
                out.print("\"gender\":\"" + rs.getString("gender") + "\",");
                out.print("\"account_type\":\"" + rs.getString("account_type") + "\",");
                out.print("\"balance\":\"" + rs.getDouble("balance") + "\"");
                out.print("}");
            } else {
                out.print("{\"error\":\"Account not found\"}");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Server error\"}");
        }
    }
}
