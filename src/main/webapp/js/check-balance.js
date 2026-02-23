window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    const balance = params.get("balance");

    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Balance Details 💳",
            html: `
                <p>Your account balance is</p>
                <p style="margin-top:10px;font-size:22px;color:green;">
                    <b>₹ ${balance}</b>
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "balance.html");
        });
    }

    if (status === "invalid") {
        Swal.fire({
            icon: "error",
            title: "Invalid Account ❌",
            text: "This account does not belong to you or does not exist",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "balance.html");
        });
    }

    if (status === "error") {
        Swal.fire({
            icon: "error",
            title: "Failed ❌",
            text: "Something went wrong. Please try again.",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "balance.html");
        });
    }
};
