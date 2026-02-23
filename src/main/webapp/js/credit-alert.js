window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    const amount = params.get("amount");

    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Credited Successfully 💰",
            html: `
                <p>Your amount has been credited successfully</p>
                <p style="margin-top:10px;font-size:18px;color:green;">
                    <b>Credited Amount:</b> ₹ ${amount}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "credit.html");
        });
    }

    if (status === "invalid_account") {
        Swal.fire({
            icon: "error",
            title: "Invalid Account ❌",
            text: "You can credit money only into your own account.",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "credit.html");
        });
    }

    if (status === "error") {
        Swal.fire({
            icon: "error",
            title: "Failed ❌",
            text: "Something went wrong. Please try again.",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "credit.html");
        });
    }
};
