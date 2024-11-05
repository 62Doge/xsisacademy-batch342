function openAddForm() {
    $.ajax({
        type: "get",
        url: "/patient/addForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Tambah Pasien</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="savePatientButton" type="button" class="btn btn-primary">Simpan</button>
            `);
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function openDeleteModal() {
    $.ajax({
        type: "get",
        url: "/patient/deleteModal",
        contentType: "html",
        success: function (deleteModal) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Hapus Pasien</strong>`);
            $('#baseModalBody').html(deleteModal);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="deletePatientButton" type="button" class="btn btn-primary">Hapus</button>
            `);
        }
    });
}

function openEditForm() {
    $.ajax({
        type: "get",
        url: "/patient/editForm",
        contentType: "html",
        success: function (editForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Edit Pasien</strong>`);
            $('#baseModalBody').html(editForm);
            $('#baseModalFooter').html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="editPatientButton" type="button" class="btn btn-primary">Hapus</button>
            `);
        }
    });
}