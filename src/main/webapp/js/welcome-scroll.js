window.addEventListener("scroll", () => {
    const strip = document.querySelector(".welcome-strip");

    if (!strip) return;

    if (window.scrollY > 10) {
        strip.classList.add("scrolled");
    } else {
        strip.classList.remove("scrolled");
    }
});
