let currentPage = 1;
let dataPerPage = 5;
let sortBy = 'name';
let sortDir = 'ASC';
let totalPages;

$(document).ready(function () {
    loadData();
    $('#searchBankInput').on('input', function () {
        const searchQuery = $(this).val();
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
        url: `http://localhost:9001/api/admin/bank/name/${query}`,
        contentType: "application/json",
        success: function (response) {
            let bankData = response.data;
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
        },
        error: function (error) {
            console.log("Error searching bank: ", error);
        }
    });
}

function loadData() {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/bank/active?page=${currentPage-1}&size=${dataPerPage}&sortBy=${sortBy}&sortDir=${sortDir}`,
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
            if (totalPages > 1) {
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
            }
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function moveToPage(pageNumber) {
    currentPage = pageNumber;
    loadData();
}

function nextPage() {
    if (currentPage + 1 <= totalPages) {
        currentPage++;
    }
    loadData();
}

function previousPage() {
    if (currentPage - 1 >= 0) {
        currentPage--;
    }
    loadData();
}

function setDataPerPage(query) {
    dataPerPage = query;
    loadData();
}

function setPageOrder() {
    sortBy = $('input[name="orderColumnRadio"]:checked').val();
    sortDir = $('input[name="orderTypeRadio"]:checked').val();
    loadData();
}

function customDataPerPage() {
    let query = $('#dataPerPageInput').val();
    console.log(query);
    dataPerPage = query;
    loadData();
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
        url: "http://localhost:9001/api/admin/bank",
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
                url: `http://localhost:9001/api/admin/bank/${id}`,
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
        url: `http://localhost:9001/api/admin/bank/update/${id}`,
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
            console.error(error);
        }
    });
}
