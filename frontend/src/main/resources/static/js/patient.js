let selectedPatient = [];
let currentPage = 1;
let pageSize = 5;
let sortBy = 'fullname';
let sortDir = 'ASC';
let totalPages;
let currentSearchQuery;

$(document).ready(function () {
    loadData();
    $('#searchPatientInput').on('input', function () {
        let searchQuery = $(this).val();
        currentSearchQuery = searchQuery;
        currentPage = 1;
        if (searchQuery) {
            searchPatient(searchQuery);
        } else {
            $('#patientTable').empty();
            loadData();
        }
    });
})

function searchPatient(query) {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/patient/name/${query}`,
        contentType: "application/json",
        success: function (response) {
            console.log(response);
            let patientData = response.data.content;
            totalPages = response.data.totalPages;

            let tableData = ``;
            patientData.forEach(patient => {
                let patientRelation = "";
                if (patient.customerRelationId === 1) {
                    patientRelation += "Diri Sendiri";
                } else if (patient.customerRelationId === 2) {
                    patientRelation += "Suami";
                } else if (patient.customerRelationId === 3) {
                    patientRelation += "Ibu";
                } else if (patient.customerRelationId === 4) {
                    patientRelation += "Anak";
                }
                let patientAge = calculateAge(patient.dob);
                tableData += `
                    <tr>
                        <td>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="">
                            </div>
                        </td>
                        <td>
                            <strong>${patient.fullName}</strong><br>
                            ${patientRelation}, ${patientAge} tahun<br>
                            <span class="text-muted">9 Chat Online, 5 Janji Online</span>
                        </td>
                        <td>
                            <button onclick="openEditForm(${patient.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${patient.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `
            });
            $('#patientTable').html(tableData);

            $('#pageList').empty();
            $('#pageList').append(`
                <li class="page-item prev" id="previousPageControl">
                    <a class="page-link" href="javascript:previousPage();"><i class='bx bx-chevron-left'></i></a>
                </li>
            `);

            for (let pageNum = 1; pageNum <= totalPages; pageNum++) {
                if (pageNum == currentPage) {
                    $('#pageList').append(`
                        <li class="page-item active">
                            <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
                        </li>
                    `);
                } else {
                    $('#pageList').append(`
                        <li class="page-item">
                            <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
                        </li>
                    `);
                }
            }
            
            $('#pageList').append(`
                <li class="page-item next" id="nextPageControl">
                    <a class="page-link" href="javascript:nextPage();"><i class='bx bx-chevron-right'></i></i></a>
                </li>
            `);
            
            // dropdown button default value
            $('input[name="orderColumnRadio"][value="' + sortBy + '"]').prop("checked", true);
            $('input[name="orderTypeRadio"][value="' + sortDir + '"]').prop("checked", true);
        },
        error: function (error) {
            console.log("Error searching patient: ", error);
        }
    });
}

function loadData() {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/patient/active?page=${currentPage-1}&size=${pageSize}&sortBy=${sortBy}&sortDir=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            console.log(response);
            let patientData = response.data.content;
            totalPages = response.data.totalPages;

            $('#patientTable').empty();

            patientData.forEach(patient => {
                let patientRelation = "";
                if (patient.customerRelationId === 1) {
                    patientRelation += "Diri Sendiri";
                } else if (patient.customerRelationId === 2) {
                    patientRelation += "Suami";
                } else if (patient.customerRelationId === 3) {
                    patientRelation += "Ibu";
                } else if (patient.customerRelationId === 4) {
                    patientRelation += "Anak";
                }
                let patientAge = calculateAge(patient.dob);
                $('#patientTable').append(`
                    <tr>
                        <td>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="">
                            </div>
                        </td>
                        <td>
                            <strong>${patient.fullName}</strong><br>
                            ${patientRelation}, ${patientAge} tahun<br>
                            <span class="text-muted">9 Chat Online, 5 Janji Online</span>
                        </td>
                        <td>
                            <button onclick="openEditForm(${patient.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${patient.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `);
            });

            $('#pageList').empty();
            $('#pageList').append(`
                <li class="page-item prev" id="previousPageControl">
                    <a class="page-link" href="javascript:previousPage();"><i class='bx bx-chevron-left'></i></a>
                </li>
            `);

            for (let pageNum = 1; pageNum <= totalPages; pageNum++) {
                if (pageNum == currentPage) {
                    $('#pageList').append(`
                        <li class="page-item active">
                            <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
                        </li>
                    `);
                } else {
                    $('#pageList').append(`
                        <li class="page-item">
                            <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
                        </li>
                    `);
                }
            }

            $('#pageList').append(`
                <li class="page-item next" id="nextPageControl">
                    <a class="page-link" href="javascript:nextPage();"><i class='bx bx-chevron-right'></i></i></a>
                </li>
            `);

            $('input[name="orderColumnRadio"][value="' + sortBy + '"]').prop("checked", true);
            $('input[name="orderTypeRadio"][value="' + sortDir + '"]').prop("checked", true);
        },
        error:  function (error) {
            console.log(error);
        }
    });
}

function moveToPage(pageNumber) {
    currentPage = pageNumber;
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function nextPage() {
    if (currentPage + 1 <= totalPages) {
        currentPage++;
    }
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function previousPage() {
    if (currentPage - 1 >= 0) {
        currentPage--;
    }
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageSize(query) {
    pageSize = query;
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageOrder() {
    sortBy = $('input[name="orderColumnRadio"]:checked').val();
    sortDir = $('input[name="orderTypeRadio"]:checked').val();
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function customPageSize() {
    let query = $('#pageSizeInput').val();
    pageSize = query;
    if (currentSearchQuery) {
        searchPatient(currentSearchQuery);
    } else {
        loadData();
    }
}

function calculateAge(birthdate) {
    const today = new Date();
    const birthDate = new Date(birthdate);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/patient/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Pasien</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="savePatientButton" type="button" class="btn btn-primary">Simpan</button>
            `);
            $('#savePatientButton').on('click', function () {
                addPatient();
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function addPatient() {
    let patientJSON = {
        "fullName": $('#addPatientName').val(),
        "dob": $('#addPatientBirthday').val(),
        "gender": $('input[name="addPatientGender"]:checked').val(),
        "blood": $('#addPatientBlood').val(),
        "rhesus": $('input[name="addPatientRhesus"]:checked').val(),
        "height": $('#addPatientHeight').val(),
        "weight": $('#addPatientWeight').val(),
        "relation": $('#addPatientRelation').val()
    }
    $.ajax({
        type: "post",
        url: "http://localhost:9001/api/patient",
        data: JSON.stringify(patientJSON),
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

function openEditForm(id) {
    $.ajax({
        type: "get",
        url: "/patient/editForm",
        contentType: "html",
        success: function (editForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Edit Pasien</strong>`);
            $('#baseModalBody').html(editForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="editPatientButton" type="button" class="btn btn-primary">Simpan</button>
            `);
            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/patient/${id}`,
                contentType: "application/json",
                success: function (response) {
                    console.log(response);
                    let patient = response.data;
                    let patientBlood = "";
                    let patientRelation = "";
                    if (patient.bloodGroupId === 1) {
                        patientBlood += "A";
                    } else if (patient.bloodGroupId === 2) {
                        patientBlood += "B";
                    } else if (patient.bloodGroupId === 3) {
                        patientBlood += "O";
                    } else if (patient.bloodGroupId === 4) {
                        patientBlood += "AB";
                    }
                    if (patient.customerRelationId === 1) {
                        patientRelation += "Diri Sendiri"
                    } else if (patient.customerRelationId === 2) {
                        patientRelation += "Suami"
                    } else if (patient.customerRelationId === 3) {
                        patientRelation += "Istri"
                    } else if (patient.customerRelationId === 4) {
                        patientRelation += "Anak"
                    }
                    $('#editPatientName').val(patient.fullName);
                    $('#editPatientBirthday').val(patient.dob);
                    $(`input[name="editPatientGender"][value="${patient.gender}"]`).prop("checked", true);
                    $('select[name="editPatientBlood"]').val(patientBlood);
                    $(`input[name="editPatientRhesus"][value="${patient.rhesus}"]`).prop("checked", true);
                    $('#editPatientHeight').val(patient.height);
                    $('#editPatientWeight').val(patient.weight);
                    $('select[name="editPatientRelation"]').val(patientRelation);
                },
                error: function (error) {
                    console.log(error);
                }
            });
            $('#editPatientButton').on('click', function () {
                editPatient(id);
            });
        }
    });
}

function editPatient(id) {
    let editPatientJSON = {
        "fullName": $('#editPatientName').val(),
        "dob": $('#editPatientBirthday').val(),
        "gender": $('input[name="editPatientGender"]:checked').val(),
        "blood": $('#editPatientBlood').val(),
        "rhesus": $('input[name="editPatientRhesus"]:checked').val(),
        "height": $('#editPatientHeight').val(),
        "weight": $('#editPatientWeight').val(),
        "relation": $('#editPatientRelation').val()
    }
    $.ajax({
        type: "put",
        url: `http://localhost:9001/api/patient/${id}`,
        data: JSON.stringify(editPatientJSON),
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

function openDeleteModal(id) {
    $.ajax({
        type: "get",
        url: "/patient/deleteModal",
        contentType: "html",
        success: function (deleteModal) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Pasien</strong>`);
            $('#baseModalBody').html(deleteModal);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="deletePatientButton" type="button" class="btn btn-primary">Hapus</button>
            `);
            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/patient/${id}`,
                contentType: "application/json",
                success: function (response) {
                    console.log(response);
                    let patientName = response.data.fullName;
                    $('#deletedPatientList').append(`
                        <li>${patientName}</li>
                    `);
                },
                error: function (error) {
                    console.log(error);
                }
            });
            $('#deletePatientButton').on('click', function () {
                deletePatient(id);
            });
        }
    });
}

function deletePatient(id) {
    $.ajax({
        type: "patch",
        url: `http://localhost:9001/api/customer-member/delete/${id}`,
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
