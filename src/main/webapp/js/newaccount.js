window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
    const accNo = params.get("accNo");

    // ✅ SUCCESS CASE
    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Account Created 🎉",
            html: `
                <p>Your account has been created successfully in State Bank of India</p>
                <p style="margin-top:10px;font-size:18px;color:green;">
                    <b>Your Account Number:</b> ${accNo}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "newaccount.html");
        });
    }

    // ⚠️ USER ALREADY HAS ACCOUNT
    if (status === "already_created") {
        Swal.fire({
            icon: "info",
            title: "Account Already Created 🏦",
            html: `
                <p>
                    Your account is already created in 
                    <b>State Bank of India</b>.
                </p>
                <p style="margin-top:10px;font-size:18px;color:#0b5ed7;">
                    <b>Your Account Number:</b> ${accNo}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "newaccount.html");
        });
    }

    // ⚠️ AADHAR ALREADY EXISTS
    if (status === "aadhar_exists") {
        Swal.fire({
            icon: "warning",
            title: "Aadhar Already Registered ⚠️",
            html: `
                <p>
                    This Aadhar number is already registered with 
                    <b>State Bank of India</b>.
                </p>
                <p style="margin-top:10px;font-size:18px;color:#0b5ed7;">
                    <b>Account Number:</b> ${accNo}
                </p>
            `,
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "newaccount.html");
        });
    }

    // ❌ ERROR CASE
    if (status === "error") {
        Swal.fire({
            icon: "error",
            title: "Failed ❌",
            text: "Something went wrong. Please try again.",
            confirmButtonText: "OK"
        }).then(() => {
            window.history.replaceState({}, document.title, "newaccount.html");
        });
    }
};
