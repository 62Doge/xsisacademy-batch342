let IS_USER_LOGGED = false;
let USER_LOGGED_ID = 3;
let ADMIN_LOGGED_ID = 0;

document.addEventListener("DOMContentLoaded", (event) => {
  // Mengecek jika pengguna tidak masuk atau bukan admin
  if (USER_LOGGED_ID !== -1 && USER_LOGGED_ID !== null) {
    IS_USER_LOGGED = true;
    $("#sidebarToggle").removeClass("d-none"); // Menampilkan tombol sidebar
    // Menampilkan sementara sidebar dan navbar
    $('#layout-menu').removeClass('d-none');
    $('#layout-navbar').removeClass('d-none');

    // mengaktifkan semua elemen dalam "col-md-8"
    $(".col-md-8 *").prop("disabled", false).css("pointer-events", "");

    $('#profile-image-upload').removeClass('d-none');
  } else {
    IS_USER_LOGGED = false;
    $("#sidebarToggle").addClass("d-none"); // Menyembunyikan tombol sidebar

    // Menghilangkan sementara sidebar dan navbar
    $('#layout-menu').addClass('d-none');
    $('#layout-navbar').addClass('d-none');

    // Menonaktifkan semua elemen dalam "col-md-8"
    $(".col-md-8 *").prop("disabled", true).css("pointer-events", "none");

    $('#profile-image-upload').addClass('d-none');
    
  }

});

