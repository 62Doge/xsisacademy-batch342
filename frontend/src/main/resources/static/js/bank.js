let currentPage = 1;
let pageSize = 5;
let sortBy = 'name';
let sortDir = 'ASC';
let totalPages;
let currentSearchQuery;

$(document).ready(function () {
    loadData();
    $('#searchBankInput').on('input', function () {
        let searchQuery = $(this).val();
        currentSearchQuery = searchQuery;
        currentPage = 1;
        if (searchQuery) {
            searchBank(searchQuery);
        } else {
            $('#bankTable').empty();
            loadData();
        }
    });
})

function searchBank(query) {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/bank/name/${query}?page=${currentPage - 1}&size=${pageSize}&sortBy=${sortBy}&sortDir=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            console.log(response);
            let bankData = response.data.content;
            totalPages = response.data.totalPages;

            let tableData = ``;
            bankData.forEach(bank => {
                tableData += `
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
                `
            });
            $('#bankTable').html(tableData);

            // show pages
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
            console.log("Error searching bank: ", error);
        }
    });
}

function loadData() {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/bank/active?page=${currentPage - 1}&size=${pageSize}&sortBy=${sortBy}&sortDir=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            let bankData = response.data.content;
            totalPages = response.data.totalPages;

            $('#bankTable').empty();

            bankData.forEach(bank => {
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

            // show pages
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
            console.log(error);
        }
    });
}

function moveToPage(pageNumber) {
    currentPage = pageNumber;
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

function nextPage() {
    if (currentPage + 1 <= totalPages) {
        currentPage++;
    }
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

function previousPage() {
    if (currentPage - 1 >= 0) {
        currentPage--;
    }
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageSize(query) {
    pageSize = query;
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageOrder() {
    sortBy = $('input[name="orderColumnRadio"]:checked').val();
    sortDir = $('input[name="orderTypeRadio"]:checked').val();
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

function customPageSize() {
    let query = $('#pageSizeInput').val();
    console.log(query);
    pageSize = query;
    if (currentSearchQuery) {
        searchBank(currentSearchQuery);
    } else {
        loadData();
    }
}

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
                <button id="saveBankButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $('#saveBankButton').on('click', function () {
                let isFormValid = true;
                let nameField = $('#addBankName').val().trim().replace(/\s+/g, ' ');
                let vaField = $('#addBankVA').val().trim().replace(/\s+/g, ' ');

                if (!nameField) {
                    $('#addNameValidation').html("Nama tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#addNameValidation').html("");
                }

                if (!vaField) {
                    $('#addVaCodeValidation').html("Kode VA tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#addVaCodeValidation').html("");
                }

                if (isFormValid) {
                    addBank();
                }
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function addBank() {
    let nameVal = $('#addBankName').val().trim().replace(/\s+/g, ' ');
    let vaCodeVal = $('#addBankVA').val().trim().replace(/\s+/g, ' ');
    let addBankJSON = {
        "name": nameVal,
        "vaCode": vaCodeVal
    }
    $.ajax({
        type: "post",
        url: "http://localhost:9001/api/admin/bank",
        data: JSON.stringify(addBankJSON),
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            let errorMessage = error.responseJSON.message;
            if (errorMessage == "Bank already exist") {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Nama Bank sudah digunakan. Silakan gunakan nama lain.</div>`);
            } else if (errorMessage == "VA Code already exist") {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Kode VA sudah digunakan. Silakan gunakan Kode VA lain.</div>`);
            } else {
                $('#addFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menyimpan Bank</div>`);
            }
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
                <button id="editBankButton" type="button" class="btn btn-primary">Simpan</button>
            `);

            $.ajax({
                type: "get",
                url: `http://localhost:9001/api/admin/bank/${id}`,
                contentType: "application/json",
                success: function (response) {
                    let bank = response.data;
                    $('#editBankName').val(bank.name);
                    $('#editBankVA').val(bank.vaCode);
                },
                error: function (error) {
                    console.log(error);
                }
            });

            $('#editBankButton').on('click', function () {
                let isFormValid = true;
                let nameField = $('#editBankName').val().trim().replace(/\s+/g, ' ');
                let vaField = $('#editBankVA').val().trim().replace(/\s+/g, ' ');

                if (!nameField) {
                    $('#editNameValidation').html("Nama tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#editNameValidation').html("");
                }

                if (!vaField) {
                    $('#editVaCodeValidation').html("Kode VA tidak boleh kosong");
                    isFormValid = false;
                } else {
                    $('#editVaCodeValidation').html("");
                }

                if (isFormValid) {
                    editBank(id);
                }
            });
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function editBank(id) {
    let nameVal = $('#editBankName').val().trim().replace(/\s+/g, ' ');
    let vaCodeVal = $('#editBankVA').val().trim().replace(/\s+/g, ' ');
    let editBankJSON = {
        "name": nameVal,
        "vaCode": vaCodeVal
    }
    $.ajax({
        type: "put",
        url: `http://localhost:9001/api/admin/bank/update/${id}`,
        data: JSON.stringify(editBankJSON),
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.log(error);
            let errorMessage = error.responseJSON.message;
            if (errorMessage == "Bank name already exists") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Nama Bank sudah digunakan. Silakan gunakan nama yang lain.</div>`);
            } else if (errorMessage == "VA Code already exists") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Kode VA sudah digunakan. Silakan gunakan Kode VA yang lain.</div>`);
            } else if (errorMessage == "Bank not found") {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Bank tidak dapat ditemukan</div>`);
            } else {
                $('#editFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menyimpan Bank</div>`);
            }
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
                url: `http://localhost:9001/api/admin/bank/${id}`,
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
        type: "patch",
        url: `http://localhost:9001/api/admin/bank/delete/${id}`,
        contentType: "application/json",
        success: function () {
            location.reload();
        },
        error: function (error) {
            let errorMessage = error.responseJSON.message;
            if (errorMessage == "Bank not found") {
                $('#deleteFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Bank tidak dapat ditemukan.</div>`);
            } else {
                $('#deleteFormValidation').html(`<div class="alert alert-danger" role="alert">Terjadi kesalahan. Gagal menghapus Bank</div>`);
            }
        }
    });
}
