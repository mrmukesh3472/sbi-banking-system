document.addEventListener("DOMContentLoaded", () => {

    fetch("AdminAllTransactionsServlet")
        .then(res => res.json())
        .then(data => {

            const tbody = document.getElementById("txnTableBody");
            tbody.innerHTML = "";

            if (data.length === 0) {
                tbody.innerHTML =
                    `<tr><td colspan="7">No transactions found</td></tr>`;
                return;
            }

            data.forEach((t, i) => {
                tbody.innerHTML += `
                    <tr>
                        <td>${i + 1}</td>
                        <td>${t.username}</td>
                        <td>${t.type}</td>
                        <td>₹ ${t.amount}</td>
                        <td>${t.accNo}</td>
                        <td>${t.relatedAcc || "-"}</td>
                        <td>${t.date}</td>
                    </tr>
                `;
            });
        });
});
