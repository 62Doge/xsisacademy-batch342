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
                <button onclick="saveNewBank()" id="saveBankBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function openEditForm() {
    $.ajax({
        type: "get",
        url: "/bank/editForm",
        contentType: "html",
        success: function (editForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Edit Bank</strong>`);
            $('#baseModalBody').html(editForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button onclick="saveNewBank()" id="saveBankBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
        }
    });
}

function openDeleteModal() {
    $.ajax({
        type: "get",
        url: "/bank/deleteModal",
        contentType: "html",
        success: function (deleteModal) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Bank</strong>`);
            $('#baseModalBody').html(deleteModal);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Tidak
                </button>
                <button onclick="saveNewBank()" id="saveBankBtn" type="button" class="btn btn-primary">Ya</button>
            `);
        }
    });
}