$(document).ready(function() {
    loadData();

    $('#searchLocationLevel').on('input', function() {
        const searchQuery = $(this).val();
        if (searchQuery) {
            searchLocationLevel(searchQuery);
        } else {
            loadData();
        }
    });

});

function searchLocationLevel(name) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/location-level/name/${name}`,
        contentType: "application/json",
        success: function(response) {
            console.log(response);
            let locationLevelData = response.data;
            let tableData = ``;

            locationLevelData.forEach(locationLevel => {
                tableData += `
                  <tr>
                    <td>${locationLevel.name}</td>
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
                `;
            });

            $('#location-level-table').html(tableData);
        },
        error: function(error) {
            console.error("Error searching location levels:", error);
        }
    });
}


function loadData() {
    let tableData = ``;
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/admin/location-level?pageNo=0",
        contentType: "application/json",
        success: function (locationLevelResponse) {
            console.log(locationLevelResponse);
            // fixed routing to get content in Paging
            let locationLevelData = locationLevelResponse.data.content;

            locationLevelData.forEach((locationLevel, index) => {
                tableData += `
                  <tr>
                    <td>${locationLevel.name}</td>
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
                `;
            });

            $('#location-level-table').html(tableData);
        },
        error: function (xhr, status, error) {
            console.error("Error loading data:", error);
        }
    });
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
                <button onclick="saveLocationLevel()" id="saveBankBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function saveLocationLevel() {
    let name = $('#locationLevelName').val();
    let abbreviation = $('#locationLevelAbbreviation').val();

    if (!name.trim()) {
        alert("Nama Level Lokasi harus diisi");
        return;
    }

    let jsonData = { name, abbreviation };
    $.ajax({
        type: "POST",
        url: "http://localhost:9001/api/admin/location-level",
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            location.reload();
        },
        error: function (error) {
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
        alert("Nama Level Lokasi harus diisi");
        return;
    }

    let jsonData = { name, abbreviation };
    $.ajax({
        type: "PUT",
        url: `http://localhost:9001/api/admin/location-level/update/${id}`,
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            console.error("Failed to update location level:", error);
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
        type: "DELETE",
        url: `http://localhost:9001/api/admin/location-level/delete/${id}`,
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            console.error("Failed to delete location level:", error);
        }
    });
}



