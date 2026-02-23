window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    const amount = params.get("amount");

    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Debited Successfully 💸",
            html: `
                <p>Your amount has been debited successfully</p>
                <p style="margin-top:10px;font-size:18px;color:red;">
                    <b>Debited Amount:</b> ₹ ${amount}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "debit.html");
        });
    }

    if (status === "lowbalance") {
        Swal.fire({
            icon: "warning",
            title: "Low Balance ⚠️",
            text: "You do not have sufficient balance to debit this amount",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "debit.html");
        });
    }

    if (status === "invalid_account") {
        Swal.fire({
            icon: "error",
            title: "Invalid Account ❌",
            text: "This account does not belong to you or does not exist",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "debit.html");
        });
    }
};
