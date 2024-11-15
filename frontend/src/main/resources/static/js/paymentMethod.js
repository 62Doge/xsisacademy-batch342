let currentPage = 1;
let pageSize = 5;
let sortBy = 'name';
let sortDir = 'asc';
let totalPages;
let currentSearchQuery;

$(document).ready(function () {
    const savedPageSize = localStorage.getItem('pageSize');
    if (savedPageSize) {
        pageSize = parseInt(savedPageSize, 10);
    }

    loadData();
    $('#searchPaymentMethod').on('input', function () {
        let searchQuery = $(this).val();
        currentSearchQuery = searchQuery;
        currentPage = 1;
        if (searchQuery) {
            searchPaymentMethod(searchQuery);
        } else {
            $('#payment-method-table').empty();
            loadData();
        }
    });
})

function searchPaymentMethod(query) {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/payment-method/name/${query}?pageNo=${currentPage-1}&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            let paymentMethodData = response.data.content;
            totalPages = response.data.metadata.totalPages;


            let tableData = ``;
            paymentMethodData.forEach(paymentMethod => {
                tableData += `
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${paymentMethod.name}</strong></td>
                        <td>
                            <button onclick="openEditForm(${paymentMethod.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${paymentMethod.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `
            });
            $('#payment-method-table').html(tableData);

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
            console.log("Error searching Payment Method: ", error);
        }
    });
}


function loadData() {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/payment-method?pageNo=${currentPage-1}&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            let paymentMethodData = response.data.content;
            totalPages = response.data.metadata.totalPages;

            $('#payment-method-table').empty();

            paymentMethodData.forEach(paymentMethod => {
                $('#payment-method-table').append(`
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${paymentMethod.name}</strong></td>
                        <td>
                            <button onclick="openEditForm(${paymentMethod.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${paymentMethod.id})" type="button" class="btn btn-icon btn-outline-danger">
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
                    console.log(pageNum);
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
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }
}

function nextPage() {
    if (currentPage + 1 <= totalPages) {
        currentPage++;
    }
    if (currentSearchQuery) {
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }
}


function previousPage() {
    if (currentPage - 1 >= 0) {
        currentPage--;
    }
    if (currentSearchQuery) {
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageSize(query) {
    pageSize = query;
    localStorage.setItem('pageSize', pageSize);
    if (currentSearchQuery) {
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageOrder() {
    sortDir = $('input[name="orderTypeRadio"]:checked').val();
    if (currentSearchQuery) {
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }
}

function customPageSize() {
    let query = $('#pageSizeInput').val();
    pageSize = query;
    localStorage.setItem('pageSize', pageSize);
    if (currentSearchQuery) {
        searchPaymentMethod(currentSearchQuery);
    } else {
        loadData();
    }

}

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/payment-method/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Cara Pembayaran</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button onclick="savePaymentMethod()" id="savePaymentMethodBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function savePaymentMethod() {
    let name = $('#paymentMethodName').val().trim();

    if (!name.trim()) {
        $('#paymentMethodNameError').text("Nama level lokasi wajib diisi.").show();
        return;
    }

    let jsonData = { name };
    $.ajax({
        type: "POST",
        url: "http://localhost:9001/api/admin/payment-method",
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            setAndShowSuccessModal("Berhasil menyimpan data!");
            location.reload();
        },
        error: function (error) {
            if (error.status === 409) {
                $('#paymentMethodNameError').text("Nama cara pembayaran sudah ada.").show();
            } else {
                alert("Failed to save Payment Method. Please try again later.");
            }
            console.error(error);
        }
    });
}

function openEditForm(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/payment-method/${id}`,
        contentType: "application/json",
        success: function (response) {
            let paymentMethod = response.data;

            // load modal
            $.ajax({
                type: "get",
                url: "/payment-method/editForm",
                contentType: "html",
                success: function (editForm) {
                    $('#baseModal').modal('show');
                    $('#baseModalTitle').html(`<strong>Edit Cara Pembayaran</strong>`);
                    $('#baseModalBody').html(editForm);

                    $('#paymentMethodName').val(paymentMethod.name);

                    $('#baseModalFooter').html(`
                        <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                            Batal
                        </button>
                        <button onclick="updatePaymentMethod(${id})" type="button" class="btn btn-primary">
                            Simpan
                        </button>
                    `);
                }
            });
        },
        error: function (error) {
            console.error("Failed to load Payment Method data:", error);
        }
    });
}


function updatePaymentMethod(id) {
    let name = $('#paymentMethodName').val();

    if (!name.trim()) {
        $('#paymentMethodNameError').text("Nama level lokasi wajib diisi.").show();
        return;
    }

    let jsonData = { name};
    $.ajax({
        type: "PUT",
        url: `http://localhost:9001/api/admin/payment-method/update/${id}`,
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            setAndShowSuccessModal("Berhasil mengupdate data!");
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            if (error.status === 400) {
                $('#paymentMethodNameError').text("Nama cara pembayaran sudah ada.").show();
            } else {
                alert("Failed to update Payment Method. Please try again later.");
            }
            console.error(error);
        }
    });
}


function openDeleteModal(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/payment-method/${id}`,
        contentType: "application/json",
        success: function (response) {
            let paymentMethod = response.data;

            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Level Lokasi</strong>`);
            $('#baseModalBody').html(`
                <div>
                    Anda akan menghapus <span id="paymentMethodName">${paymentMethod.name}</span>?
                </div>
            `);

            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button onclick="deletePaymentMethod(${id})" type="button" class="btn btn-danger">
                    Ya
                </button>
            `);
        },
        error: function (error) {
            console.error("Failed to load payment method data for deletion:", error);
        }
    });
}

function deletePaymentMethod(id) {
    $.ajax({
        type: "PATCH",
        url: `http://localhost:9001/api/admin/payment-method/soft-delete/${id}`,
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            if (error.status === 409) {
                alert("Tidak bisa menghapus cara pembayaran, data tersebut masih digunakan.");
            } else {
                alert("Failed to delete Payment Method. Please try again later.");
            }
            console.log(error);
        }
    });
}

function setAndShowSuccessModal(modalBody) {
    $("#successModalBody").html(modalBody);
    $("#successModal").modal("show");
}



