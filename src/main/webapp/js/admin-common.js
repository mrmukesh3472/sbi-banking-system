document.addEventListener("DOMContentLoaded", function () {

    const hamburger = document.querySelector(".hamburger");
    const menu = document.querySelector(".menu");

    if (!hamburger || !menu) {
        console.warn("Hamburger or Menu not found");
        return;
    }

    hamburger.addEventListener("click", function () {
        menu.classList.toggle("show");
    });

});
