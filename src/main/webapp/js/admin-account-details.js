
document.getElementById("adminAccountForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const accNo = document.getElementById("accNoInput").value;

    fetch("AccountDetailsServlet?accNo=" + accNo)
        .then(res => res.json())
        .then(data => {

            if (data.error) {
                alert(data.error);
                return;
            }

            document.getElementById("infoBox").style.display = "block";

            document.getElementById("dAccNo").innerText = data.acc_no;
            document.getElementById("username").innerText = data.username;
            document.getElementById("father").innerText = data.father_name;
            document.getElementById("email").innerText = data.email;
            document.getElementById("mobile").innerText = data.mobile;
            document.getElementById("gender").innerText = data.gender;
            document.getElementById("type").innerText = data.account_type;
            document.getElementById("balance").innerText = "₹ " + data.balance;
        })
        .catch(err => {
            console.error(err);
            alert("Server error");
        });
});
