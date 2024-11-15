$(document).ready(function () {
    loadData();
    $('#searchBloodInput').on('input', function () {
        let searchQuery = $(this).val();
        if (searchQuery) {
            searchBlood(searchQuery);
        } else {
            $('#bloodGroupTable').empty();
            loadData();
        }
    });
})

function searchBlood(query) {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/blood-group/code/${query}`,
        contentType: "application/json",
        success: function (response) {
            let bloodData = response.data;
            console.log(bloodData);
            
            let tableData = ``;
            $('#bloodGroupTable').empty();
            bloodData.forEach(bloodGroup => {
                let bloodDesc = bloodGroup.description;
                if (!bloodDesc) {
                    bloodDesc = "";
                }
                tableData += `
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${bloodGroup.code}</strong></td>
                        <td>${bloodDesc}</td>
                        <td>
                            <button onclick="openEditForm(${bloodGroup.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${bloodGroup.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `
                $('#bloodGroupTable').html(tableData);
            });
        }
    });
}

function loadData() {
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/admin/blood-group/active",
        contentType: "application/json",
        success: function (response) {
            let bloodGroupData = response.data;
            bloodGroupData.forEach(bloodGroup => {
                let bloodDesc = bloodGroup.description;
                if (!bloodDesc) {
                    bloodDesc = "";
                }
                $('#bloodGroupTable').append(`
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${bloodGroup.code}</strong></td>
                        <td>${bloodDesc}</td>
                        <td>
                            <button onclick="openEditForm(${bloodGroup.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${bloodGroup.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `);
            });
        }
    });
}

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/blood-group/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Golongan Darah</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="saveBloodGroupButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $('#saveBloodGroupButton').on('click', function () {
                let isFormValid = true;
                let codeField = $('#addBloodCode').val();

                if(!codeField) {
                    $('#addCodeValidation').html("Kode tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#addCodeValidation').html("");
                }

                if (isFormValid) {
                    addBloodGroup();
                }
            });
        }
    });
}

function addBloodGroup() {
    let codeVal = $('#addBloodCode').val().trim().replace(/\s+/g, ' ');
    let bloodGroupJSON = {
        "code": codeVal,
        "description": $('#addBloodDesc').val()
    }
    $.ajax({
        type: "post",
        url: "http://localhost:9001/api/admin/blood-group",
        data: JSON.stringify(bloodGroupJSON),
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.log(error);
            let errorMessage = error.responseJSON.message;
            if (errorMessage == "Blood Group already exists") {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Kode sudah digunakan. Silakan gunakan kode lain.</div>`);
            } else if (errorMessage == "Code input exceed character limit of 5") {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Input kode melebihi batas karakter maksimum 5.</div>`);
            } else {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menyimpan Golongan Darah</div>`);
            }
        }
    });
}

function openEditForm(id) {
    $.ajax({
        type: "get",
        url: "/blood-group/editForm",
        contentType: "html",
        success: function (editForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Edit Golongan Darah</strong>`);
            $('#baseModalBody').html(editForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Batal
                </button>
                <button id="editBloodGroupButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/admin/blood-group/${id}`,
                contentType: "application/json",
                success: function (response) {
                    let bloodGroup = response.data;
                    $('#editBloodCode').val(bloodGroup.code);
                    $('#editBloodDesc').val(bloodGroup.description);
                },
                error: function (error) {
                    console.log(error);
                }
            });

            $('#editBloodGroupButton').on('click', function () {
                let isFormValid = true;
                let codeField = $('#editBloodCode').val();

                if(!codeField) {
                    $('#editCodeValidation').html("Kode tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#editCodeValidation').html("");
                }

                if (isFormValid) {
                    editBloodGroup(id);
                }
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function editBloodGroup(id) {
    let codeVal = $('#editBloodCode').val().trim().replace(/\s+/g, ' ');
    let editBloodGroupJSON = {
        "code": codeVal,
        "description": $('#editBloodDesc').val()
    }
    $.ajax({
        type: "put",
        url: `http://localhost:9001/api/admin/blood-group/update/${id}`,
        data: JSON.stringify(editBloodGroupJSON),
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.log(error);
            let errorMessage = error.responseJSON.message;
            if (errorMessage == "Blood Group not found") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Golongan darah tidak dapat ditemukan</div>`);
            } else if (errorMessage == "Code input exceed character limit of 5") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Input kode melebihi batas karakter maksimum 5.</div>`);
            } else if (errorMessage == "Blood Group already exists") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Kode sudah digunakan. Silakan gunakan kode lain.</div>`);
            } else {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menyimpan golongan darah</div>`);
            }
        }
    });
}

function openDeleteModal(id) {
    $.ajax({
        type: "get",
        url: `/blood-group/deleteModal`,
        contentType: "html",
        success: function (deleteModal) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Golongan Darah</strong>`);
            $('#baseModalBody').html(deleteModal);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button id="deleteBloodGroupButton" type="button" class="btn btn-primary">Ya</button>
            `);

            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/admin/blood-group/${id}`,
                contentType: "application/json",
                success: function (response) {
                    let bloodGroup = response.data;
                    $('#bloodGroupDeleteSpan').html(bloodGroup.code);
                },
                error: function (error) {
                    console.log(error);
                }
            });

            $('#deleteBloodGroupButton').on('click', function () {
                deleteBloodGroup(id);
            });
        }
    });
}

function deleteBloodGroup(id) {
    $.ajax({
        type: "patch",
        url: `http://localhost:9001/api/admin/blood-group/delete/${id}`,
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.error(error);
        }
    });
}
