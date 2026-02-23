const params = new URLSearchParams(window.location.search);
const status = params.get("status");

if (status === "success") {
    Swal.fire({
        icon: "success",
        title: "Password Changed",
        text: "Your password has been updated successfully",
        confirmButtonText: "OK"
    });
}

if (status === "wrongold") {
    Swal.fire({
        icon: "error",
        title: "Wrong Password",
        text: "Old password is incorrect",
        confirmButtonText: "Try Again"
    });
}

if (status === "mismatch") {
    Swal.fire({
        icon: "warning",
        title: "Password Mismatch",
        text: "New password and confirm password do not match",
        confirmButtonText: "OK"
    });
}

if (status === "loginrequired") {
    Swal.fire({
        icon: "warning",
        title: "Login Required",
        text: "Please login again",
        confirmButtonText: "OK"
    });
}

if (status === "error") {
    Swal.fire({
        icon: "error",
        title: "Error",
        text: "Something went wrong. Please try again.",
        confirmButtonText: "OK"
    });
}
