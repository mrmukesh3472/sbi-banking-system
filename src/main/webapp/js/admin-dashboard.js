document.addEventListener("DOMContentLoaded", () => {

    fetch("admin/dashboard-data")
        .then(response => response.json())
        .then(data => {
            const totalUserElement =
                document.querySelector(".info-row strong");

            totalUserElement.textContent = data.totalUsers;
        })
        .catch(error => {
            console.error("Error fetching dashboard data:", error);
        });

});
