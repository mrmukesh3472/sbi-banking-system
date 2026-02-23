package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class DebitServlet extends HttpServlet {

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
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (amount <= 0) {
            resp.sendRedirect("debit.html?status=invalid_amount");
            return;
        }

        try {
            // ✅ MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ MySQL Connection
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sbi_bank",
                "root",
                "root"
            );

            // ✅ account check
            PreparedStatement check = con.prepareStatement(
                "SELECT balance FROM accounts WHERE acc_no=? AND username=?"
            );
            check.setLong(1, accNo);
            check.setString(2, username);

            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                con.close();
                resp.sendRedirect("debit.html?status=invalid_account");
                return;
            }

            double balance = rs.getDouble("balance");
            if (balance < amount) {
                con.close();
                resp.sendRedirect("debit.html?status=lowbalance");
                return;
            }

            // ✅ debit balance
            PreparedStatement debit = con.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE acc_no=?"
            );
            debit.setDouble(1, amount);
            debit.setLong(2, accNo);
            debit.executeUpdate();

            // ✅ transaction history
            PreparedStatement history = con.prepareStatement(
                "INSERT INTO transactions (acc_no, username, trans_type, amount, related_acc) " +
                "VALUES (?,?,?,?,?)"
            );
            history.setLong(1, accNo);
            history.setString(2, username);
            history.setString(3, "DEBIT");
            history.setDouble(4, amount);
            history.setNull(5, Types.BIGINT);  // MySQL compatible
            history.executeUpdate();

            con.close();
            resp.sendRedirect("debit.html?status=success&amount=" + amount);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("debit.html?status=error");
        }
    }
}
