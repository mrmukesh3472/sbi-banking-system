package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class TransferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        String senderUsername = (String) session.getAttribute("username");

        long senderAcc = Long.parseLong(req.getParameter("senderAcc"));
        long receiverAcc = Long.parseLong(req.getParameter("receiverAcc"));
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (senderAcc == receiverAcc) {
            resp.sendRedirect("transfer.html?status=invalid_receiver");
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

            // ===== SENDER CHECK =====
            PreparedStatement psSender = con.prepareStatement(
                    "SELECT balance FROM accounts WHERE acc_no=? AND username=?"
            );
            psSender.setLong(1, senderAcc);
            psSender.setString(2, senderUsername);
            ResultSet rsSender = psSender.executeQuery();

            if (!rsSender.next()) {
                con.close();
                resp.sendRedirect("transfer.html?status=invalid_sender");
                return;
            }

            if (rsSender.getDouble("balance") < amount) {
                con.close();
                resp.sendRedirect("transfer.html?status=lowbalance");
                return;
            }

            // ===== RECEIVER CHECK + USERNAME =====
            PreparedStatement psReceiver = con.prepareStatement(
                    "SELECT username FROM accounts WHERE acc_no=?"
            );
            psReceiver.setLong(1, receiverAcc);
            ResultSet rsReceiver = psReceiver.executeQuery();

            if (!rsReceiver.next()) {
                con.close();
                resp.sendRedirect("transfer.html?status=invalid_receiver");
                return;
            }

            String receiverUsername = rsReceiver.getString("username");

            con.setAutoCommit(false);

            // ===== UPDATE BALANCE =====
            PreparedStatement debit = con.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE acc_no=?"
            );
            debit.setDouble(1, amount);
            debit.setLong(2, senderAcc);
            debit.executeUpdate();

            PreparedStatement credit = con.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE acc_no=?"
            );
            credit.setDouble(1, amount);
            credit.setLong(2, receiverAcc);
            credit.executeUpdate();

            // ===== TRANSACTION HISTORY =====
            PreparedStatement h1 = con.prepareStatement(
                    "INSERT INTO transactions(acc_no, username, trans_type, amount, related_acc) " +
                    "VALUES (?,?,?,?,?)"
            );
            h1.setLong(1, senderAcc);
            h1.setString(2, senderUsername);
            h1.setString(3, "TRANSFER-DEBIT");
            h1.setDouble(4, amount);
            h1.setLong(5, receiverAcc);
            h1.executeUpdate();

            PreparedStatement h2 = con.prepareStatement(
                    "INSERT INTO transactions(acc_no, username, trans_type, amount, related_acc) " +
                    "VALUES (?,?,?,?,?)"
            );
            h2.setLong(1, receiverAcc);
            h2.setString(2, receiverUsername);
            h2.setString(3, "TRANSFER-CREDIT");
            h2.setDouble(4, amount);
            h2.setLong(5, senderAcc);
            h2.executeUpdate();

            con.commit();
            con.close();

            resp.sendRedirect("transfer.html?status=success&amount=" + amount);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("transfer.html?status=error");
        }
    }
}
