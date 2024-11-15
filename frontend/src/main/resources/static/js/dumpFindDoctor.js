$(document).ready(function () {
    loadData();
})

function loadData() {
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/doctor/doctor-self",
        contentType: "application/json",
        success: function (response) {
            const dataList = response.data;
            dataList.forEach(data => {
                $('#doctorList').append(`
                    <div class="col-6 col-md-4 mb-4">
                      <div class="card shadow-sm border-0">
                        <img
                          src="https://via.placeholder.com/150"
                          class="card-img-top"
                          alt="${data.biodata.fullname}"
                        />
                        <div class="card-body text-center">
                          <h5 class="card-title text-secondary mb-3">
                            ${data.biodata.fullname}
                          </h5>
                          <div class="d-flex justify-content-center gap-2 mt-3">
                            <button data-doctor-id="1" class="btn btn-outline-primary btn-sm" onclick="showDoctorDetails(${data.id})">Detail</button>
                            <button class="btn btn-outline-success btn-sm">Chat</button>
                            <button
                              data-doctor-id="${data.id}"
                              class="btn btn-outline-warning btn-sm appointment-button"
                            >
                              Buat Janji
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                            `);
            });
        }
    });
}

function showDoctorDetails(doctorId) {
    window.location.href = `/doctor-details?doctorId=` + doctorId;
}