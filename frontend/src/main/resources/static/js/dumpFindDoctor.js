$(document).ready(function () {
    loadData();
})

function loadData() {
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/doctor/doctor-self",
        contentType: "application/json",
        success: function (response) {
            console.log(response);
        }
    });
}

function showDoctor(doctorId) {
    
}