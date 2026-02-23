fetch("DashboardServlet")
  .then(res => res.json())
  .then(data => {

    if (data.status === "unauthorized") {
        window.location.href = "login.html";
        return;
    }

    // ✅ ALWAYS PRINT ACCOUNT HOLDER
    document.getElementById("userName").innerText = data.accountHolder || "Users";
    document.getElementById("accHolder").innerText = data.accountHolder || "-";

    if (data.status === "no_account") {

        document.getElementById("accNo").innerText = "-";
        document.getElementById("accType").innerText = "-";
        document.getElementById("fatherName").innerText = "-";
        document.getElementById("balance").innerText = "0";

        document.getElementById("accountMsg").innerHTML =
          "<span style='color:red'>No account created yet. Please create a new account.</span>";
    }

    if (data.status === "account_exists") {

        document.getElementById("accountMsg").innerHTML = "";

        document.getElementById("accNo").innerText = data.accNo;
        document.getElementById("accType").innerText = data.accountType;
        document.getElementById("fatherName").innerText = data.fatherName;

        animateBalance(parseInt(data.balance));
    }
  })
  .catch(err => console.error(err));

function animateBalance(amount) {
    let val = 0;
    let timer = setInterval(() => {
        if (val >= amount) {
            val = amount;
            clearInterval(timer);
        }
        document.getElementById("balance").innerText = val;
        val += Math.ceil(amount / 40);
    }, 25);
}
