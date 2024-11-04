// frontend/src/main/resources/static/js/doctorProfile.js
function showTindakan(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#tindakan").addClass("active");
}

function showSpesialisasi(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#spesialisasi").addClass("active");
}

function showAktifitas(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#aktifitas").addClass("active");
}

function showKonsultasi(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#konsultasi").addClass("active");
}

function showPengaturan(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#pengaturan").addClass("active");
}

