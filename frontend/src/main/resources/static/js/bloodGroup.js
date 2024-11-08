$(document).ready(function () {
    loadData();
})

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
                addBloodGroup();
            });
        }
    });
}

function addBloodGroup() {
    let bloodGroupJSON = {
        "code": $('#addBloodCode').val(),
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
                editBloodGroup(id);
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function editBloodGroup(id) {
    let editBloodGroupJSON = {
        "code": $('#editBloodCode').val(),
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
