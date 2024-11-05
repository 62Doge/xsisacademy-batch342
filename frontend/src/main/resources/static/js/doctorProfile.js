
const id = 3;

getData(id);
function getData(id){
  getDoctorEducation(id);
  getDoctorOffice(id);
  getDoctorTreatment(id)
  getCurrentDoctorSpecialization(id);
  getDoctor(id);
}

function getDoctorEducation(id){
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/doctor-education/${id}`,
    contentType: "application/json",
    success: function (doctorEducationResponse) {
      let educationData = "";
      doctorEducationResponse.data.forEach(value => {
        educationData += `
          <div class= "flex-container">${value.institutionName}</div>
          <div style="display: flex; justify-content: space-between">${value.major}
          <span style="margin-left: auto">${value.endYear}</span>
          </div><br>
        `
      });
      $("#doctor-education").html(educationData);
    }
  });
}

function getDoctorOffice(id) {
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/doctor-office/${id}`,
    contentType: "application/json",
    success: function (doctorOfficeResponse) {
      let officeData = "";
      doctorOfficeResponse.data.forEach(value => {
        let yearEnd = new Date(value.endDate);
        let yearEndDate = yearEnd.getFullYear();
        let endDate = value.endDate ? yearEndDate:'Sekarang';
        
        let yearStart = new Date(value.startDate);
        let yearStartDate = yearStart.getFullYear();
        
        officeData += `
          <div class= "flex-container">${value.medicalFacility.name}</div>
          <div style="display: flex; justify-content: space-between">${value.specialization}
          <span style="margin-left: auto">${yearStartDate} - ${endDate}</span>
          </div><br>
        `
      });
      $("#doctor-office").html(officeData);
    }
  });
}

function getDoctorTreatment(id) {
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/doctor-treatment/${id}`,
    contentType: "application/json",
    success: function (doctorTreatmentResponse) {
      let treatmentData = "";
      doctorTreatmentResponse.data.forEach(value => {
        
        treatmentData += `
          <li>${value.name}</li>
        `
      });
      $("#doctor-treatment").html(treatmentData);
    }
  });
}

function getCurrentDoctorSpecialization(id) {
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/current-doctor-specialization/${id}`,
    contentType: "application/json",
    success: function (currentDoctorSpecializationResponse) {
      let dataSpecialization = currentDoctorSpecializationResponse.data[0];
      $('#doctor-specialization').html(dataSpecialization.specialization.name);
    }
  });
}

function getDoctor(id){
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/doctor-self/${id}`,
    contentType: "application/json",
    success: function (doctorResponse) {
      let biodataDoctor = doctorResponse.data;
      console.log(biodataDoctor);
      $('#doctor-name').html(biodataDoctor.biodata.fullname);
      $('#doctor-image').html(`<img alt="Profile picture of ${biodataDoctor.biodata.fullname}" height="100" src="${biodataDoctor.biodata.imagePath}" width="100" style="width: 100px; height: 100px; border-radius: 50%;">`);
      
    }
  });
}

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

