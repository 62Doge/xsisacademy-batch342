$(document).ready(function () {
  const activeTab = localStorage.getItem("activeTab")||'tindakan';
  const activeNavLink = localStorage.getItem("activeNavLink")||'nav_link_tindakan';
  

  if (activeTab) {
      $(".tab-pane").removeClass("active");
      $(`#${activeTab}`).addClass("active");

      $(".nav-link").removeClass("active");
      $(`[href="#${activeTab}"]`).addClass("active");
  }

  if(activeNavLink){
    $('.nav-link').removeClass('active');
    $(`#${activeNavLink}`).addClass('active');
  }
});



const id = 3;

getData(id);
function getData(id){
  getDoctorEducation(id);
  getDoctorOffice(id);
  getDoctorTreatment(id)
  getCurrentDoctorSpecialization(id);
  getDoctor(id);
  getLengthAppointmentByDoctorId(id)
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

// treatment dokter
function getDoctorTreatment(id) {
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/doctor-treatment/${id}`,
    contentType: "application/json",
    success: function (doctorTreatmentResponse) {
      let treatmentDataSideBar = "";
      let treatmentDataMenuBar = "";

      doctorTreatmentResponse.data.forEach(value => {
        
        treatmentDataSideBar += `
          <li>${value.name}</li>
        `

        treatmentDataMenuBar += `
          <div class="bg-secondary fs-5 text-white px-2 my-2 d-inline-flex align-items-center rounded" id="treatment-${value.id}">
              ${value.name}
              <button class="btn-close ms-2" onclick="removeFormTreatment(${value.id})"></button>
          </div>
        `
      },
    );
      $("#doctor-treatment").html(treatmentDataSideBar);
      $('#kontenTindakan').html(treatmentDataMenuBar);
    }
  });
}
function addFormTreatment() {
  $.ajax({
    type: "get",
    url: "/doctor-profile/addFormTreatment",
    contentType: "html",
    success: function (addFormTreatment) {
      $('#baseModal').modal('show');
      $('#baseModalTitle').html('<strong class="fs-2">Tambahkan Tindakan</strong>');
      $('#baseModalBody').html(addFormTreatment);
      $('#treatment').on('input', function(){
        $('#treatment').next('p').remove();
        $('#treatment').css("border-color", "");
      });
      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button id="saveTreatmentButton" type="button" class="btn btn-primary">Simpan</button>
        `);
      $('#saveTreatmentButton').on('click', function () {
        const treatment = $('#treatment').val();

        if(treatment.length > 0){
          
          addTreatment();
        }
        else{
          if(!$('#treatment-error').length){
            $('#treatment').after(`<p id="treatment-error" class="text-danger">Kolom inputan tidak boleh kosong</p>`)
          }
          $('#treatment').css("border-color", "red");
        }
      })
    },
    error: function (error) {
      console.log(error);
    }
  });
}

function addTreatment() {
  let treatmentDataJSON = {
    name: $('#treatment').val().trim().replace(/\s+/g, ' '),
    doctorId: id
  }
  $.ajax({
    type: "POST",
    url: "http://localhost:9001/api/doctor/doctor-treatment",
    data: JSON.stringify(treatmentDataJSON),
    contentType: "application/json",
    success: function () {
      $('#baseModalTitle').html(`<strong class="fs-2">Sukses</strong>`);
      $('#baseModalBody').html(`<p class="text-center fs-3">Tindakan berhasil ditambahkan</p>`);
      $('#baseModalFooter').html("");
    },
    error: function (error) { 
      let errorMessage = error.responseJSON.message;
      if(errorMessage == "doctor treatment already exist"){
        $('#addFormTreatmentValidation').html(`<div class="alert alert-danger" role="alert">Tindakan sudah terdaftar. Silakan Masukan Tindakan lain.</div>`);
      }else{
        $('#addFormTreatmentValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menambahkan Tindakan</div>`);
      }
    },
    complete: function () {
      $('#baseModal').on('hidden.bs.modal', function () {
        localStorage.setItem("activeTab", "tindakan");
        localStorage.setItem("activeNavLink", "nav_link_tindakan");
        window.location.reload();

      });
    }
  });
}


function removeFormTreatment(id) {
  $.ajax({
    type: "get",
    url: `/doctor-profile/deleteFormTreatment`,
    contentType: "html",
    success: function (deleteFormTreatment) {

      $('#baseModal').modal('show');
      $('#baseModalTitle').html(`<strong class="fs-2">Hapus Tindakan</strong>`);
      $('#baseModalBody').html(deleteFormTreatment);
      $.ajax({
        type: "get",
        url: `http://localhost:9001/api/doctor/doctor-treatment/treatment/${id}`,
        contentType: "application/json",
        success: function (treatmentData) {
          console.log(treatmentData.data);
          
          $('#treatment').html(`<p class="text-center fs-3">Anda Setuju Untuk Menghapus Tindakan ${treatmentData.data.name} ?</p>`);
        }
      });
      
      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button id="hapusTreatmentButton" type="button" class="btn btn-primary" onclick="removeTreatment(${id})">Hapus</button>
        `);
      },
    error: function (error) {
      console.log(error);
    }
  });
}

function removeTreatment(id) {
  $.ajax({
    type: "PATCH",
    url: `http://localhost:9001/api/doctor/doctor-treatment/delete/${id}`,
    contentType: "application/json",
    success: function () {
      $('#baseModalTitle').html(`<strong class="fs-2">Sukses</strong>`);
      $('#baseModalBody').html(`<p class="text-center fs-3">Tindakan berhasil dihapus</p>`);
      $('#baseModalFooter').html("");
      
    },
    error: function (error) {
      console.log(error)
    },
    complete: function () {
      $('#baseModal').on('hidden.bs.modal', function () {
        localStorage.setItem("activeTab", "tindakan");
        localStorage.setItem("activeNavLink", "nav_link_spesialisasi");
        window.location.reload();
      });
    }
  });
}

// spesialisasi dokter
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
      <button class="btn btn-icon btn-outline-warning" type="button" onclick="editFormSpecialization(${id})"><span class="tf-icons bx bxs-edit"></span></button>
      `);
    },
    error: function (error) {
      if(error.status === 404) {
        $('#spesialisasi').html(`
          <h2>Anda belum menambahkan spesialisasi</h2>
          <button class="btn btn-primary" onclick="addFormSpecialization()"><i class="bx bx-add-to-queue"></i>Tambah Spesialisasi</button>
          `);
      }else{
        console.error("Error fetching specialization:", error.statusText);
      }
    }
  });
}

function addFormSpecialization(){
  $.ajax({
    type: "get",
    url: "/doctor-profile/addFormSpecialization",
    contentType: "html",
    success: function (addFormSpecialization) {
      $('#baseModal').modal('show');
      $('#baseModalTitle').html(`<strong class="fs-2">Pilih Spesialisasi Anda</strong>`);
      $('#baseModalBody').html(addFormSpecialization);

      $.ajax({
        type: "get",
        url: "http://localhost:9001/api/doctor/specialization",
        contentType: "application/json",
        success: function (specializationResponse) {
          let dataSpecialization = specializationResponse.data;
          let options = ``;
          options += `<option selected value="0">Select Spesialisasi</option>`
          dataSpecialization.forEach(value => {
            options += `<option value="${value.id}">${value.name}</option>`
          });
          $('#addSpecialization').html(options);
          $('#addSpecialization').on('change', function() {
            $('#addSpecialization').next('p').remove();
            $('#addSpecialization').css("border-color", "");
          });
        }
      });
      
      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button id="saveSpecializationButton" type="button" class="btn btn-primary">Simpan</button>
      `);
      $('#saveSpecializationButton').on('click', function() {
        const specializationValue = $('#addSpecialization').val();
        if (specializationValue && specializationValue !== "0") {
          addSpecialization();
        } else {
          if (!$('#specialization-error').length) {
            $('#addSpecialization').after(`<p id="specialization-error" class="text-danger">Pilih spesialisasi Anda</p>`);
          }
          $('#addSpecialization').css("border-color", "red");
        }
      });
    },
    error: function(error) {
      console.log(error);
    }
  });
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
      $('#baseModalTitle').html(`<strong class="fs-2">Sukses</strong>`);
      $('#baseModalBody').html(`<p class="text-center fs-3">Spesialisasi berhasil ditambahkan</p>`);
      $('#baseModalFooter').html("");
    },
    error: function (error) {
      console.log(error);
    },
    complete: function () {
      $('#baseModal').on('hidden.bs.modal', function () {
        localStorage.setItem("activeTab", "spesialisasi");
        localStorage.setItem("activeNavLink", "nav_link_spesialisasi");
        window.location.reload();
      });
    }
  });
}

function editFormSpecialization(id) {
  $.ajax({
    type: "get",
    url: "/doctor-profile/editFormSpecialization",
    contentType: "html",
    success: function (editForm) {
      $('#baseModal').modal('show');
      $('#baseModalTitle').html(`<strong class="fs-2">Pilih Spesialisasi Anda</strong>`);
      $('#baseModalBody').html(editForm);

      $.ajax({
        type: "get",
        url: `http://localhost:9001/api/doctor/current-doctor-specialization/${id}`,
        contentType: "application/json",
        success: function (specializationResponseId) {
          let dataSpecializationId = specializationResponseId.data[0];
          console.log(dataSpecializationId);
          
          let options = `<option value="${dataSpecializationId.specializationId}" selected>${dataSpecializationId.specialization.name}</option>`
          $('#editSpecialization').html(options);
          $('#editSpecialization').on('change', function() {
            $('#editSpecialization').next('p').remove();
            $('#editSpecialization').css("border-color", "");
          });

          $.ajax({
            type: "get",
            url: "http://localhost:9001/api/doctor/specialization",
            contentType: "application/json",
            success: function (specializationResponse) {
              let dataSpecialization = specializationResponse.data;
              console.log(dataSpecialization);
              
              let options = ``;
              options += `<option value="0">Select Spesialisasi</option>`
              dataSpecialization.forEach(value => {
                if(value.id !== dataSpecializationId.specializationId) {
                  options += `<option value="${value.id}">${value.name}</option>`
                }
              });
              $('#editSpecialization').append(options);
            }
          });
        }
      });

      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button id="saveSpecializationButton" type="button" class="btn btn-primary">Simpan</button>
      `);

      $('#saveSpecializationButton').on('click', function() {
        const specializationValue = $('#editSpecialization').val();
        if(specializationValue !== "0") {
          editSpecialization(id);
        }else{
          if (!$('#specialization-error').length) {
            $('#editSpecialization').after(`<p id="specialization-error" class="text-danger">Pilih spesialisasi Anda</p>`);
          }
          $('#editSpecialization').css("border-color", "red");
        }
      });
    }
  });

}

function editSpecialization(id) {
  let spcializationDataJSON = {
    doctorId: id,
    specializationId: $('#editSpecialization').val()
  }
  $.ajax({
    type: "PUT",
    url: `http://localhost:9001/api/doctor/current-doctor-specialization/update/${id}`,
    data: JSON.stringify(spcializationDataJSON),
    contentType: "application/json",
    success: function (response) {
      console.log(response);
      $('#baseModalTitle').html(`<strong class="fs-2">Sukses</strong>`);
      $('#baseModalBody').html(`<p class="text-center fs-3">Spesialisasi berhasil diubah</p>`);
      $('#baseModalFooter').html("");
    },
    error: function (error) {
      console.log(error);
    },
    complete: function () {
      $('#baseModal').on('hidden.bs.modal', function () {
        localStorage.setItem("activeTab", "spesialisasi");
        localStorage.setItem("activeNavLink", "nav_link_spesialisasi");
        window.location.reload();
      });
    }
  });
}

// profile dokter
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

function getLengthAppointmentByDoctorId(id) { 
  $.ajax({
    type: "get",
    url: `http://localhost:9001/api/doctor/appointment/${id}`,
    contentTypeType: "application",
    success: function (response) {
      let appointmentCount = response.data.length;
      console.log(appointmentCount);
      $('#appointment-count').text(appointmentCount);
    },
    error: function (error) {
      console.error("Error fetching specialization:", error);
    }
  });
 }

function showTindakan(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#tindakan").addClass("active");
  localStorage.setItem("activeNavLink", "nav_link_tindakan");
  localStorage.setItem("activeTab","tindakan");
}

function showSpesialisasi(event,element) {
  event.preventDefault();
  $(".nav-link").removeClass("active");
  $(element).addClass("active");
  $(".tab-pane").removeClass("active");
  $("#spesialisasi").addClass("active");
  localStorage.setItem("activeNavLink", "nav_link_spesialisasi");
  localStorage.setItem("activeTab","spesialisasi");
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

function triggerImageUpload() {
  document.getElementById('doctorImageUpload').click();
}

function uploadDoctorImage(event) {
  const file = event.target.files[0];
  const doctorId = 1;
  
  if (file) {
    const formData = new FormData();
    formData.append("image", file);
    formData.append("doctorId", doctorId);

    $.ajax({
      type: "POST",
      url: `http://localhost:9001/api/doctor/upload-image/${doctorId}`,
      data: formData,
      contentType: false,
      processData: false,
      success: function (response) {
        $("#uploadStatus").text("Gambar berhasil diunggah!");
      },
      error: function (error) {
        console.error("Error uploading image:", error);
        $("#uploadStatus").text("Gagal mengunggah gambar.");
      }
    });
  } else {
    $("#uploadStatus").text("Tidak ada gambar yang dipilih.");
  }
}


