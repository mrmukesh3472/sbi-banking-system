window.onload = function () {
    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");

    // ===== USER LOGIN =====
    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Login Successful 🎉",
            text: "Welcome to SBI Online Banking",
            confirmButtonText: "Go to Dashboard"
        }).then(() => {
            window.location.href = "dashboard.html";
        });
    }

    // ===== ADMIN LOGIN =====
    if (status === "adminsuccess") {
        Swal.fire({
            icon: "success",
            title: "Welcome Admin 👋",
            text: "You have logged in successfully",
            confirmButtonText: "Go to Dashboard"
        }).then(() => {
            window.location.href = "admin-dashboard.html";
        });
    }

    if (status === "invalid") {
        Swal.fire({
            icon: "error",
            title: "Invalid Credentials ❌",
            text: "Username or Password is incorrect",
            confirmButtonText: "Try Again"
        });
    }

    if (status === "unauthorized") {
        Swal.fire({
            icon: "warning",
            title: "Access Denied ⚠️",
            text: "You are not authorized as Admin",
            confirmButtonText: "OK"
        });
    }

    if (status === "error") {
        Swal.fire({
            icon: "error",
            title: "Server Error",
            text: "Something went wrong. Please try again.",
            confirmButtonText: "OK"
        });
    }
};
