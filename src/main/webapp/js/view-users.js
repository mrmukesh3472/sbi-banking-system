document.addEventListener("DOMContentLoaded", () => {

    fetch("ViewUsersServlet")
        .then(res => res.json())
        .then(data => {

            const tbody = document.getElementById("userTableBody");
            tbody.innerHTML = "";

            data.forEach((user, index) => {

                // Admin ko block mat dikhao
                let actionBtn = "—";

                if (user.username.toLowerCase() !== "admin") {

                    let btnText = user.status === "BLOCKED" ? "Unblock" : "Block";
                    let btnClass = user.status === "BLOCKED"
                        ? "unblock-btn"
                        : "block-btn";

                    actionBtn = `
                        <button class="${btnClass}"
                            onclick="confirmAction('${user.username}','${btnText.toUpperCase()}')">
                            ${btnText}
                        </button>
                    `;
                }

                const row = `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.contact}</td>
                        <td class="${user.status === 'BLOCKED' ? 'blocked' : 'active'}">
                            ${user.status}
                        </td>
                        <td>${actionBtn}</td>
                    </tr>
                `;

                tbody.innerHTML += row;
            });
        })
        .catch(err => console.error(err));
});

function confirmAction(username, action) {

    Swal.fire({
        title: "Are you sure?",
        text: `Do you want to ${action} this user?`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes",
    }).then((result) => {

        if (result.isConfirmed) {

            fetch("BlockUnblockServlet", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `username=${username}&action=${action}`
            })
            .then(res => res.text())
            .then(msg => {
                if (msg === "success") {
                    Swal.fire("Updated!", "User status changed.", "success")
                        .then(() => location.reload());
                }
            });
        }
    });
}
