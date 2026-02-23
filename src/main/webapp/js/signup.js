window.onload = function () {

    const params = new URLSearchParams(window.location.search);
    const status = params.get("status");
	
	// Success popup
    if (status === "success") {
        Swal.fire({
            icon: "success",
            title: "Registration Successful 🎉",
            text: "Your registration successfully completed",
            confirmButtonText: "Go to Login"
        }).then(() => {
            window.location.href = "login.html";
        });
    }
	
	// Error popup
    if (status === "error") {
        Swal.fire({
            icon: "error",
            title: "Registration Failed ❌",
            text: "Something went wrong. Please try again",
            confirmButtonText: "OK"
        });
    }
};

