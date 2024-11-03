$(document).ready(function () {
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/bank/active",
        contentType: "application/json",
        success: function (response) {
            let bankResponse = response.data;
            bankResponse.forEach(bank => {
                $('#bankTable').append(`
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${bank.name}</strong></td>
                        <td>${bank.vaCode}</td>
                        <td>
                            <button onclick="openEditForm(${bank.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${bank.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `);
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
})

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/bank/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Bank</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button disabled id="saveBankButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $('#addBankName, #addBankVA').on('input', toggleButtonState);

            function toggleButtonState() {
                const nameFilled = $('#addBankName').val().trim() !== '';
                const vaFilled = $('#addBankVA').val().trim() !== '';
                $('#saveBankButton').prop('disabled', !(nameFilled && vaFilled));
            }

            $('#saveBankButton').on('click', function () {
                addBank();
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function addBank() {
    let addBankJSON = {
        "name": $('#addBankName').val(),
        "vaCode": $('#addBankVA').val()
    }
    $.ajax({
        type: "post",
        url: "http://localhost:9001/api/bank",
        data: JSON.stringify(addBankJSON),
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
        url: "/bank/editForm",
        contentType: "html",
        success: function (editForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Edit Bank</strong>`);
            $('#baseModalBody').html(editForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Batal
                </button>
                <button disabled id="editBankButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/bank/${id}`,
                contentType: "application/json",
                success: function (response) {
                    let bank = response.data;
                    $('#editBankName').val(bank.name);
                    $('#editBankVA').val(bank.vaCode);
                    toggleButtonState();
                },
                error: function (error) {
                    console.log(error);
                }
            });

            $('#editBankName, #editBankVA').on('input', toggleButtonState);

            function toggleButtonState() {
                const nameFilled = $('#editBankName').val().trim() !== '';
                const vaFilled = $('#editBankVA').val().trim() !== '';
                $('#editBankButton').prop('disabled', !(nameFilled && vaFilled));
            }

            $('#editBankButton').on('click', function () {
                editBank(id);
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function editBank(id) {
    let editBankJSON = {
        "name": $('#editBankName').val(),
        "vaCode": $('#editBankVA').val()
    }
    $.ajax({
        type: "put",
        url: `http://localhost:9001/api/bank/update/${id}`,
        data: JSON.stringify(editBankJSON),
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
        url: "/bank/deleteModal",
        contentType: "html",
        success: function (deleteModal) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Bank</strong>`);
            $('#baseModalBody').html(deleteModal);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button id="deleteBankButton" type="button" class="btn btn-primary">Ya</button>
            `);

            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/bank/${id}`,
                contentType: "application/json",
                success: function (response) {
                    let bank = response.data;
                    $('#bankNameDeleteSpan').html(bank.name);
                },
                error: function (error) {
                    console.log(error);
                }
            });

            $('#deleteBankButton').on('click', function () {
                deleteBank(id);
            });
        }
    });
}

function deleteBank(id) {
    $.ajax({
        type: "put",
        url: `http://localhost:9001/api/bank/delete/${id}`,
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.error(error);
        }
    });
}
