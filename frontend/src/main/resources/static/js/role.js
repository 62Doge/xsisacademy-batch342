$(document).ready(function() {
    loadData();

    $('#searchRoleAccess').on('input', function() {
        const searchQuery = $(this).val();
        if (searchQuery) {
            searchRoleAccess(searchQuery);
        } else {
            loadData();
        }
    });

});

function searchRoleAccess(name) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/role/name/${name}`,
        contentType: "application/json",
        success: function(response) {
            console.log(response);
            let roleAccessData = response.data.content;
            let tableData = ``;

            roleAccessData.forEach(roleAccess => {
                tableData += `
                  <tr>
                    <td>${roleAccess.name}</td>
                    <td>${roleAccess.code}</td>
                    <td>
                        <button onclick="openEditForm(${roleAccess.id})" type="button" class="btn btn-icon btn-outline-warning">
                            <span class="tf-icons bx bxs-edit"></span>
                        </button>
                        <button onclick="openDeleteModal(${roleAccess.id})" type="button" class="btn btn-icon btn-outline-danger">
                            <span class="tf-icons bx bxs-trash"></span>
                        </button>
                    </td>
                  </tr>
                `;
            });

            $('#role-access-table').html(tableData);
        },
        error: function(error) {
            console.error("Error searching roles:", error);
        }
    });
}


function loadData() {
    let tableData = ``;
    $.ajax({
        type: "get",
        url: "http://localhost:9001/api/admin/role?pageNo=0",
        contentType: "application/json",
        success: function (roleAccessResponse) {
            console.log(roleAccessResponse);
            // fixed routing to get content in Paging
            let roleAccessData = roleAccessResponse.data.content;

            roleAccessData.forEach((roleAccess, index) => {
                tableData += `
                  <tr>
                    <td>${roleAccess.name}</td>
                    <td>${roleAccess.code}</td>
                    <td>
                        <button onclick="openEditForm(${roleAccess.id})" type="button" class="btn btn-icon btn-outline-warning">
                            <span class="tf-icons bx bxs-edit"></span>
                        </button>
                        <button onclick="openDeleteModal(${roleAccess.id})" type="button" class="btn btn-icon btn-outline-danger">
                            <span class="tf-icons bx bxs-trash"></span>
                        </button>
                    </td>
                  </tr>
                `;
            });

            $('#role-access-table').html(tableData);
        },
        error: function (xhr, status, error) {
            console.error("Error loading data:", error);
        }
    });
}

function openAddForm() {
    $.ajax({
        type: "get",
        url: "/role/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Role</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button onclick="saveRoleAccess()" id="saveRoleBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function saveRoleAccess() {
    let name = $('#roleAccessName').val();
    let code = $('#roleAccessCode').val();
    let createdBy = 0;

    if (!name.trim()) {
        alert("Nama Role harus diisi");
        return;
    }

    let jsonData = { name, code, createdBy };
    $.ajax({
        type: "POST",
        url: "http://localhost:9001/api/admin/role",
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
        url: `http://localhost:9001/api/admin/role/${id}`,
        contentType: "application/json",
        success: function (response) {
            let roleAccess = response.data;

            // load modal
            $.ajax({
                type: "get",
                url: "/role/editForm",
                contentType: "html",
                success: function (editForm) {
                    $('#baseModal').modal('show');
                    $('#baseModalTitle').html(`<strong>Edit Role</strong>`);
                    $('#baseModalBody').html(editForm);

                    $('#roleAccessName').val(roleAccess.name);
                    $('#roleAccessCode').val(roleAccess.code);

                    $('#baseModalFooter').html(`
                        <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                            Batal
                        </button>
                        <button onclick="updateRoleAccess(${id})" type="button" class="btn btn-primary">
                            Simpan
                        </button>
                    `);
                }
            });
        },
        error: function (error) {
            console.error("Failed to load role data:", error);
        }
    });
}

function updateRoleAccess(id) {
    let name = $('#roleAccessName').val();
    let code = $('#roleAccessCode').val();

    if (!name.trim()) {
        alert("Nama Role harus diisi");
        return;
    }

    let jsonData = { name, code };
    $.ajax({
        type: "PUT",
        url: `http://localhost:9001/api/admin/role/update/${id}`,
        data: JSON.stringify(jsonData),
        contentType: "application/json",
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            console.error("Failed to update role:", error);
        }
    });
}

function openDeleteModal(id) {
    $.ajax({
        type: "GET",
        url: `http://localhost:9001/api/admin/role/${id}`,
        contentType: "application/json",
        success: function (response) {
            let roleAccess = response.data;

            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Role</strong>`);
            $('#baseModalBody').html(`
                <div>
                    Anda akan menghapus <span id="roleAccessName">${roleAccess.name}</span>?
                </div>
            `);

            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button onclick="deleteRoleAccess(${id})" type="button" class="btn btn-danger">
                    Ya
                </button>
            `);
        },
        error: function (error) {
            console.error("Failed to load role data for deletion:", error);
        }
    });
}

function deleteRoleAccess(id) {
    $.ajax({
        type: "PATCH",
        url: `http://localhost:9001/api/admin/role/soft-delete/${id}`,
        success: function (response) {
            $('#baseModal').modal('hide');
            loadData();
        },
        error: function (error) {
            console.error("Failed to delete role:", error);
        }
    });
}



