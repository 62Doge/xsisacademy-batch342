let IS_USER_LOGGED = false;
let USER_LOGGED_ID = 2;
let ADMIN_LOGGED_ID = 0;

document.addEventListener("DOMContentLoaded", (event) => {
  if (USER_LOGGED_ID !== -1 && USER_LOGGED_ID !== null) {
    IS_USER_LOGGED = true;
    $("#sidebarToggle").removeClass("d-none");
  } else {
    IS_USER_LOGGED = false;
    $("#sidebarToggle").addClass("d-none");
  }
});
