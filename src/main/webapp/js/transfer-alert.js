window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    const amount = params.get("amount");

    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Transfer Successful 💸",
            html: `
                <p>Money transferred successfully</p>
                <p style="margin-top:10px;font-size:18px;color:green;">
                    <b>Amount:</b> ₹ ${amount}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "transfer.html");
        });
    }

    if (status === "lowbalance") {
        Swal.fire({
            icon: "warning",
            title: "Insufficient Balance ⚠️",
            text: "You do not have sufficient balance",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "transfer.html");
        });
    }

    if (status === "invalid_sender") {
        Swal.fire({
            icon: "error",
            title: "Invalid Sender ❌",
            text: "Sender account does not belong to you or does not exist",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "transfer.html");
        });
    }

    if (status === "invalid_receiver") {
        Swal.fire({
            icon: "error",
            title: "Invalid Receiver ❌",
            text: "Receiver account does not exist",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "transfer.html");
        });
    }
};
