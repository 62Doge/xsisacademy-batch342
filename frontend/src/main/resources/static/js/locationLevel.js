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
    $('#searchLocationLevel').on('input', function () {
        let searchQuery = $(this).val();
        currentSearchQuery = searchQuery;
        currentPage = 1;
        if (searchQuery) {
            searchLocationLevel(searchQuery);
        } else {
            $('#location-level-table').empty();
            loadData();
        }
    });
})

function searchLocationLevel(query) {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/location-level/name/${query}?pageNo=${currentPage-1}&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            let locationLevelData = response.data.content;
            totalPages = response.data.metadata.totalPages;


            let tableData = ``;
            locationLevelData.forEach(locationLevel => {
                tableData += `
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${locationLevel.name}</strong></td>
                        <td>${locationLevel.abbreviation}</td>
                        <td>
                            <button onclick="openEditForm(${locationLevel.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${locationLevel.id})" type="button" class="btn btn-icon btn-outline-danger">
                                <span class="tf-icons bx bxs-trash"></span>
                            </button>
                        </td>
                    </tr>
                `
            });
            $('#location-level-table').html(tableData);

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
            console.log("Error searching Location Level: ", error);
        }
    });
}


function loadData() {
    $.ajax({
        type: "get",
        url: `http://localhost:9001/api/admin/location-level?pageNo=${currentPage-1}&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDir}`,
        contentType: "application/json",
        success: function (response) {
            let locationLevelData = response.data.content;
            totalPages = response.data.metadata.totalPages;

            $('#location-level-table').empty();

            locationLevelData.forEach(locationLevel => {
                $('#location-level-table').append(`
                    <tr>
                        <td><i class="fab fa-angular fa-lg text-danger me-3"></i><strong>${locationLevel.name}</strong></td>
                        <td>${locationLevel.abbreviation}</td>
                        <td>
                            <button onclick="openEditForm(${locationLevel.id})" type="button" class="btn btn-icon btn-outline-warning">
                                <span class="tf-icons bx bxs-edit"></span>
                            </button>
                            <button onclick="openDeleteModal(${locationLevel.id})" type="button" class="btn btn-icon btn-outline-danger">
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
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}

function setAndShowSuccessModal(modalBody) {
    $("#successModalBody").html(modalBody);
    $("#successModal").modal("show");
}

function nextPage() {
    if (currentPage + 1 <= totalPages) {
        currentPage++;
    }
    if (currentSearchQuery) {
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}


function previousPage() {
    if (currentPage - 1 >= 0) {
        currentPage--;
    }
    if (currentSearchQuery) {
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageSize(query) {
    pageSize = query;
    localStorage.setItem('pageSize', pageSize);
    if (currentSearchQuery) {
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}

function setPageOrder() {
    sortDir = $('input[name="orderTypeRadio"]:checked').val();
    // sortBy = $('input[name="orderTypeRadio"]:checked').val();
    if (currentSearchQuery) {
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}

function customPageSize() {
    let query = $('#pageSizeInput').val();
    pageSize = query;
    localStorage.setItem('pageSize', pageSize);
    if (currentSearchQuery) {
        searchLocationLevel(currentSearchQuery);
    } else {
        loadData();
    }
}

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/location-level/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Level Lokasi</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button onclick="saveLocationLevel()" id="saveLocationLevelBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function saveLocationLevel() {
    let name = $('#locationLevelName').val().trim();
    let abbreviation = $('#locationLevelAbbreviation').val();

    if (!name.trim()) {
        $('#locationLevelNameError').text("Nama level lokasi wajib diisi.").show();
        return;
    }

    let jsonData = { name, abbreviation };
    $.ajax({
        type: "POST",
        url: "http://localhost:9001/api/admin/location-level",
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            setAndShowSuccessModal("Berhasil menyimpan data!");
            location.reload();
        },
        error: function (error) {
            if (error.status === 409) {
                // alert("Location Level name already exists.");
                $('#locationLevelNameError').text("Nama level lokasi sudah ada.").show();
            } else {
                alert("Failed to save Location Level. Please try again later.");
            }
            console.error(error);
        }
    });
}

function openEditForm(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/location-level/${id}`,
        contentType: "application/json",
        success: function (response) {
            let locationLevel = response.data;

            // load modal
            $.ajax({
                type: "get",
                url: "/location-level/editForm",
                contentType: "html",
                success: function (editForm) {
                    $('#baseModal').modal('show');
                    $('#baseModalTitle').html(`<strong>Edit Level Lokasi</strong>`);
                    $('#baseModalBody').html(editForm);

                    $('#locationLevelName').val(locationLevel.name);
                    $('#locationLevelAbbreviation').val(locationLevel.abbreviation);

                    $('#baseModalFooter').html(`
                        <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                            Batal
                        </button>
                        <button onclick="updateLocationLevel(${id})" type="button" class="btn btn-primary">
                            Simpan
                        </button>
                    `);
                }
            });
        },
        error: function (error) {
            console.error("Failed to load location level data:", error);
        }
    });
}


function updateLocationLevel(id) {
    let name = $('#locationLevelName').val();
    let abbreviation = $('#locationLevelAbbreviation').val();

    if (!name.trim()) {
        $('#locationLevelNameError').text("Nama level lokasi wajib diisi.").show();
        return;
    }

    let jsonData = { name, abbreviation };
    $.ajax({
        type: "PUT",
        url: `http://localhost:9001/api/admin/location-level/update/${id}`,
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            setAndShowSuccessModal("Berhasil mengupdate data!");
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            if (error.status === 409) {
                $('#locationLevelNameError').text("Nama level lokasi sudah ada.").show();
            } else if (error.status === 400) {
                $('#locationLevelNameError').text("Nama level lokasi sudah ada.").show();
            } else {
                alert("Failed to update Location Level. Please try again later.");
            }
            console.error(error);
        }
    });
}


function openDeleteModal(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/location-level/${id}`,
        contentType: "application/json",
        success: function (response) {
            let locationLevel = response.data;

            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Level Lokasi</strong>`);
            $('#baseModalBody').html(`
                <div>
                    Anda akan menghapus <span id="locationLevelName">${locationLevel.name}</span>?
                </div>
            `);

            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button onclick="deleteLocationLevel(${id})" type="button" class="btn btn-danger">
                    Ya
                </button>
            `);
        },
        error: function (error) {
            console.error("Failed to load location level data for deletion:", error);
        }
    });
}

function deleteLocationLevel(id) {
    $.ajax({
        type: "PATCH",
        url: `http://localhost:9001/api/admin/location-level/soft-delete/${id}`,
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            if (error.status === 409) {
                alert("Cannot delete location level: it's active on locations..");
            } else {
                alert("Failed to delete Location Level. Please try again later.");
            }
            console.log(error);
        }
    });
}



