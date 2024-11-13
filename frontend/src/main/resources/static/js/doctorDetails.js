let doctorId = 3;

$(document).ready(async function () {
    await loadHeader(doctorId);
    await loadTreatment(doctorId);
    await loadEducation(doctorId);
    await loadOfficeHistory(doctorId);
    await loadOfficeLocation(doctorId);
})

function loadHeader(doctorId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "get",
            url: `http://localhost:9001/api/doctor/details/header/${doctorId}`,
            contentType: "application/json",
            success: function (response) {
                // console.log(response);
                let headerData = response.data;
                let doctorName = headerData.name;
                let doctorSpecialization = headerData.specialization;
                let doctorYOE = headerData.yearOfExperience;
                $('#doctorNameHeader').html(doctorName);
                $('#thisDoctorBreadcrumb').html(doctorName);
                $('#specializationHeader').html(doctorSpecialization);
                $('#experienceHeader').html(doctorYOE);
                resolve();
            },
            error: function (error) {
                console.error(error);
                reject();
            }
        });
    });
}

function loadTreatment(doctorId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "get",
            url: `http://localhost:9001/api/doctor/details/treatment/${doctorId}`,
            contentType: "application/json",
            success: function (response) {
                // console.log(response);
                let treatmentData = response.data;
                let treatments = treatmentData.treatments;
                treatments.forEach(treatment => {
                    $('#treatmentList').append(`
                        <li>${treatment}</li>
                    `);
                });
                resolve();
            },
            error: function (error) {
                console.error(error);
                reject();
            }
        });
    });
}

function loadEducation(doctorId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "get",
            url: `http://localhost:9001/api/doctor/details/education/${doctorId}`,
            contentType: "application/json",
            success: function (response) {
                // console.log(response);
                let educationData = response.data;
                let educations = educationData.education;
                educations.forEach(education => {
                    $('#educationList').append(`
                        <li>
                            <strong>${education.name}</strong><br>
                            ${education.major} (${education.year})<br>
                        </li>
                    `);
                });
                resolve();
            },
            error: function (error) {
                console.error(error);
                reject();
            }
        });
    });
}

function loadOfficeHistory(doctorId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "get",
            url: `http://localhost:9001/api/doctor/details/office-history/${doctorId}`,
            contentType: "application/json",
            success: function (response) {
                // console.log(response);
                let officeHistoryData = response.data;
                let officeHistories = officeHistoryData.officeHistory;
                officeHistories.forEach(officeHistory => {
                    let today = new Date();
                    let startYear = new Date(officeHistory.startDate).getFullYear();
                    let endYear;
                    if (officeHistory.endDate === null) {
                        endYear = "sekarang";
                    } else {
                        endYear = new Date(officeHistory.endDate).getFullYear();
                    }
                    $('#officeHistoryList').append(`
                        <li>
                            <strong>${officeHistory.name}</strong><br>
                            ${officeHistory.location}<br>
                            ${officeHistory.specialization}<br>
                            ${startYear} — ${endYear}
                        </li>
                    `);
                });
                resolve();
            },
            error: function (error) {
                console.error(error);
                reject();
            }
        });
    });
}

// function loadOfficeLocation(doctorId) {
//     return new Promise((resolve, reject) => {
//         $.ajax({
//             type: "get",
//             url: `http://localhost:9001/api/doctor/details/office-location/${doctorId}`,
//             contentType: "application/json",
//             success: function (response) {
//                 console.log(response);
//                 resolve();
//             },
//             error: function (error) {
//                 console.error(error);
//                 reject();
//             }
//         });
//     });
// }

function loadOfficeLocation(doctorId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "get",
            url: `http://localhost:9001/api/doctor/details/office-location/${doctorId}`,
            contentType: "application/json",
            success: function (response) {
                let officeLocationData = response.data;
                let officeLocations = officeLocationData.officeLocation;
                let count = 1;
                officeLocations.forEach(officeLocation => {
                    let medicalFacilityId = officeLocation.medicalFacilityId;
                    let price = 0;
                    let scheduleList = ``;
                    let accordionName = "accordion" + count;
                    $.ajax({
                        type: "get",
                        url: `http://localhost:9001/api/doctor/details/office-location/price-start/${medicalFacilityId}/${doctorId}`,
                        contentType: "application/json",
                        success: function (response) {
                            let priceData = response.data.priceStart;
                            price += priceData;
                            price = new Intl.NumberFormat('id-ID').format(price);
                            $.ajax({
                                type: "get",
                                url: `http://localhost:9001/api/doctor/details/office-location/schedule/${medicalFacilityId}/${doctorId}`,
                                contentType: "application/json",
                                success: function (response) {
                                    let schedules = response.data.schedules;
                                    schedules.forEach(schedule => {
                                        scheduleList += `<li>${schedule.day}: ${schedule.startTime} - ${schedule.endTime}</li>`
                                    });
                                    $('#officeLocationList').append(`
                                        <div>
                                            <div class="d-flex flex-row p-4">
                                                <div class="w-100">
                                                    <strong>${officeLocation.medicalFacilityName}</strong><br>
                                                    ${officeLocation.serviceUnitName}<br>
                                                    ${officeLocation.address}, ${officeLocation.subdistrict}, ${officeLocation.city}
                                                </div>
                                                <div class="text-end my-auto">
                                                    Konsultasi<br>Mulai Dari<br>
                                                    <strong><span>Rp${price}</span></strong>
                                                </div>
                                            </div>
                                            <div class="card accordion-item">
                                                <h2 class="accordion-header" id="headingOne">
                                                    <button type="button" class="accordion-button collapsed" data-bs-toggle="collapse"
                                                        data-bs-target="#${accordionName}" aria-expanded="false"
                                                        aria-controls="${accordionName}" style="border: 1px solid #e5e6e7;">
                                                        Lihat Jadwal Praktek
                                                    </button>
                                                </h2>
                                                <div id="${accordionName}" class="accordion-collapse collapse pt-2"
                                                    data-bs-parent="#accordionExample">
                                                    <div class="accordion-body d-flex flex-row">
                                                        <div class="w-100">
                                                            <ul>
                                                                ${scheduleList}
                                                            </ul>
                                                        </div>
                                                        <div class="text-end my-auto">
                                                            <button type="button" class="btn btn-primary text-nowrap" onclick="makeAppointment(${doctorId}, ${officeLocation.id})">Buat Janji</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    `);
                                }
                            });
                        }
                    });
                    
                    count++;
                });
                resolve();
            },
            error: function (error) {
                console.error(error);
                reject();
            }
        });
    });
}