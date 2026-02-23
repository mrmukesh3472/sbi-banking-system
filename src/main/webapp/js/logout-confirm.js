document.addEventListener("DOMContentLoaded", function () {

    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", function (e) {
            e.preventDefault();

            Swal.fire({
                title: "Are you sure?",
                text: "You want to logout from SBI Banking System",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#0b3c8a",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, Logout",
                cancelButtonText: "No"
            }).then((result) => {
                if (result.isConfirmed) {
					window.location.href = "logout";

                }
            });
        });
    }

});
