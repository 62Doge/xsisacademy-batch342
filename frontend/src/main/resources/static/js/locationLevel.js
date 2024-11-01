loadData();
function loadData() {
    let tableData = ``;
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/admin/location-level",
        contentType: "application/json",
        success: function (locationLevelResponse) {
            console.log(locationLevelResponse); // Log response to check structure
            let locationLevelData = locationLevelResponse.data;

            locationLevelData.forEach((locationLevel, index) => {
                tableData += `
                  <tr>
                    <td>${locationLevel.name}</td>
                    <td>${locationLevel.abbreviation}</td>
                    <td>
                        <button onclick="openEditForm()" type="button" class="btn btn-icon btn-outline-warning">
                            <span class="tf-icons bx bxs-edit"></span>
                        </button>
                        <button onclick="openDeleteModal()" type="button" class="btn btn-icon btn-outline-danger">
                            <span class="tf-icons bx bxs-trash"></span>
                        </button>
                    </td>
                  </tr>
                `;
            });

            $('#location-level-table').html(tableData);
        },
        error: function (xhr, status, error) {
            console.error("Error loading data:", error); // Log error if any
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
                <button onclick="saveNewBank()" id="saveBankBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}
