
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
      // to show specialization on side menu
      let dataSpecialization = currentDoctorSpecializationResponse.data[0];
      $('#doctor-specialization').html(dataSpecialization.specialization.name);
      
      // to show specialization on menu bar
      $('#spesialisasi').html(`
      <h2>${dataSpecialization.specialization.name}</h2>
      <button class="btn btn-icon btn-outline-warning" type="button" onclick="editSpecialization(${id})"><span class="tf-icons bx bxs-edit"></span></button>
      `);
    },
    error: function (error) {
      if(error.status === 404) {
        $('#spesialisasi').html(`
          <h2>Anda belum menambahkan spesialisasi</h2>
          <button class="btn btn-primary" onclick="openAddForm()"><i class="bx bx-add-to-queue"></i>Tambah Spesialisasi</button>
          `);
      }else{
        console.error("Error fetching specialization:", error.statusText);
      }
    }
  });
}

function openAddForm(){
  $.ajax({
    type: "get",
    url: "/doctor-profile/addForm",
    contentType: "html",
    success: function (addForm) {
      $('#baseModal').modal('show');
      $('#baseModalTitle').html(`<strong>Pilih Spesialisasi Anda</strong>`);
      $('#baseModalBody').html(addForm);

      $.ajax({
        type: "get",
        url: "http://localhost:9001/api/doctor/specialization",
        contentType: "application/json",
        success: function (specializationResponse) {
          let dataSpecialization = specializationResponse.data;
          let options = ``;
          options += `<option selected="true" disabled="true" value="">Select Spesialisasi</option>`
          dataSpecialization.forEach(value => {
            options += `<option value="${value.id}">${value.name}</option>`
          });
          $('#addSpecialization').append(options);
        }
      });
      
      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button disabled id="saveSpecializationButton" type="button" class="btn btn-primary">Simpan</button>
      `);
      $('#addSpecialization').on('change', function() {
        if ($(this).val() !== '') {
          $('#saveSpecializationButton').prop('disabled', false);
        } else {
          $('#saveSpecializationButton').prop('disabled', true);
        }
      });
      $('#saveSpecializationButton').on('click', function() {
        addSpecialization();
      })
    },
    error: function(error) {
      console.log(error);
    }
  });
}

function editSpecialization() {

}


function addSpecialization() {
  let spcializationDataJSON = {
    doctorId: id,
    specializationId: $('#addSpecialization').val()
  }
  $.ajax({
    type: "POST",
    url: "http://localhost:9001/api/doctor/current-doctor-specialization",
    data: JSON.stringify(spcializationDataJSON),
    contentType: "application/json",
    success: function (response) {
      console.log(response);
      location.reload();
    },
    error: function (error) {
      console.log(error);
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
      $('#doctor-name').html(`<h4>${biodataDoctor.biodata.fullname}</h4>`);
      $('#doctor-image').html(`<img alt="Profile picture of ${biodataDoctor.biodata.fullname}" height="100" src="${biodataDoctor.biodata.imagePath}" width="100" style="width: 100px; height: 100px; border-radius: 50%;">`);
      $('#doctor-name-nav').text(biodataDoctor.biodata.fullname);
      $('.doctor-avatar-nav').html(`<img src="${biodataDoctor.biodata.imagePath}" alt="Profile picture of ${biodataDoctor.biodata.fullname}" height="100" class="w-px-40 h-auto rounded-circle"/>`)
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


