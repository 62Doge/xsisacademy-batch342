$(document).ready(function () {
  loadSearchResult(0, rowPerPage, sortDirection);

  // make checbox checked when input focus
  $("#customRowPerPageInputNumber").on("focus", () => {
    $("#customRowPerPageCheckbox").prop("checked", true);
  });
});

let sortDirection = "asc";
let rowPerPage = 5;
let currentPage = 0;
let searchQuery = "";

function renderTableData(dataObj) {
  const dataTable = $("#role-table");
  dataTable.html("");

  dataObj.forEach((role) => {
    dataTable.append(`
      <tr data-id="${role.id}">
                <td>${role.name}</td>
                <td>${role.code}</td>
                <td class="text-center">
                    <button type="button" class="btn btn-info" onClick="openSetMenuAccessModal(${role.id})">
                        Atur
                    </button>
                </td>
            </tr>
      `)
  })
}

// membuat animasi saat loading memuat data
function renderPlaceholder() {
  $("#role-table").html(`
<tr class="placeholder-glow">
  <td><span class="placeholder col-8"></span></td>
  <td><span class="placeholder col-8"></span></td>
  <td class="text-center"><span class="placeholder col-8"></span></td>
</tr>
<tr class="placeholder-glow">
  <td><span class="placeholder col-8"></span></td>
  <td><span class="placeholder col-8"></span></td>
  <td class="text-center"><span class="placeholder col-8"></span></td>
</tr>
<tr class="placeholder-glow">
  <td><span class="placeholder col-8"></span></td>
  <td><span class="placeholder col-8"></span></td>
  <td class="text-center"><span class="placeholder col-8"></span></td>
</tr>
<tr class="placeholder-glow">
  <td><span class="placeholder col-8"></span></td>
  <td><span class="placeholder col-8"></span></td>
  <td class="text-center"><span class="placeholder col-8"></span></td>
</tr>
<tr class="placeholder-glow">
  <td><span class="placeholder col-8"></span></td>
  <td><span class="placeholder col-8"></span></td>
  <td class="text-center"><span class="placeholder col-8"></span></td>
</tr>
        `);
}

function renderPagination(pageInfo) {
  $('#paginationContainer').html('');
  const totalPages = pageInfo.totalPages;
  const currentPage = pageInfo.page;


  $('#paginationContainer').append(`
    <li class="page-item disabled d-flex align-items-center justify-content-center" style="margin-right: 10px;">
      <span class="page-info">
        Halaman ${currentPage + 1} dari ${totalPages}
      </span>
    </li>
    `)

  if(totalPages > 3) {
    $('#paginationContainer').append(`
      <li class="page-item prev ${0 === currentPage ? "disabled" : ""}">
        <button class="page-link nav-btn" data-page="0">
          <i class="tf-icon bx bx-chevrons-left"></i>
        </button>
      </li>
      `)
  }

  let startPage = Math.max(0, currentPage - 1);
  let endPage = Math.min(totalPages - 1, currentPage + 1);
  
  if (currentPage === 0) {
    endPage = Math.min(totalPages - 1, 2);
  } else if (currentPage === totalPages - 1) {
    startPage = Math.max(0, totalPages - 3);
  }

  for(let i = startPage; i <= endPage; i++){
    $("#paginationContainer").append(`
      <li class="page-item ${i === currentPage ? "active disabled" : ""}">
        <button class="page-link nav-btn" data-page="${i}">
          ${i + 1}
        </button>
      </li>`);
  }

  if (totalPages > 3) {
    $("#paginationContainer").append(`
    <li class="page-item prev ${totalPages - 1 === currentPage ? "disabled" : ""}">
        <button class="page-link nav-btn" data-page="${totalPages - 1}">
          <i class="tf-icon bx bx-chevrons-right"></i>
        </button>
    </li>
        `);
  }
}

function loadSearchResult(pageNo = 0, pageSize = 5, sortDirection = "asc", searchText = "") {
  renderPlaceholder();
  const sortBy = "name";

  $.ajax({
    type: "get",
    url: "http://localhost:9001/api/admin/role",
    data: {pageNo, pageSize, sortBy, sortDirection, searchText},
    dataType: "json",
    success: function (response) {
      console.log(response);
      
      renderPagination(response.data.metadata);
      renderTableData(response.data.content);
    },
    error: function (response) {
      console.log(response);
    }
  });
}

$(document).on("click", ".page-link.nav-btn", function () {
  const page = $(this).data("page");
  currentPage = page;
  loadSearchResult(page, rowPerPage, sortDirection, searchQuery);
});

function applySorting(e) {
  e.preventDefault();
  const selectedSort = $('input[name="orderTypeRadio"]:checked').data("sort");
  if (selectedSort != sortDirection) {
    sortDirection = selectedSort;
    loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
  }
}

$("#customRowPerPageInputNumber").on("input", function (event) {
  event.preventDefault();
  let inputValue = parseInt($(this).val());
  if (inputValue < 1) {
    $(this).val(1);
  }
  $("#customRowPerPageCheckbox").data("page", inputValue);
});

function applyRowPerPage(event) {
  event.preventDefault();
  const selectedPerPageVal = $('input[name="row-per-page"]:checked').data("page");
  if (selectedPerPageVal != rowPerPage) {
    rowPerPage = selectedPerPageVal;
    loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
  }
}

$('#searchRole').on("keyup", function (e) {
  if (e.key === "Enter") {
    const query = $(this).val().trim();

    if (query != searchQuery) {
      searchQuery = query;
      const data = loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
      console.log(data);
      

      $.ajax({
        type: "get",
        url: "/set-menu-access/modal",
        contentType: "html",
        success: function (modal) {
          $('#baseModal').modal('show');
          $('#baseModalTitle').html('<strong class="fs-2">Konfirmasi</strong>');
          $('#baseModalBody').html(modal);
          const dataHtml = data.length ? data.map(role => `<p>${role.name} (${role.code})</p>`).join('') : '<p>Data tidak ditemukan</p>';
          $('#modalKonfirmasi').html(`<p class="fs-4">data yang anda cari sebagi berikut:</p>${dataHtml}`);
        }
      });
    }
  }
});

function openSetMenuAccessModal(roleId) {
  
  $.ajax({
    type: "get",
    url: "/set-menu-access/updateForm",
    contentType: "html",
    success: function (updateForm) {
      $('#baseModal').modal('show');
      $('#baseModalTitle').html('<strong class="fs-2">ATUR AKSES</strong>');
      $('#baseModalBody').html(updateForm);

      // menggunakan checkbox select all
      $('#selectAll').on('change',function () {
        const isChecked = $(this).is(':checked');
        $('#accessTree .form-check-input').prop('checked', isChecked);
      });
      
      $.ajax({
        type: "get",
        url: "http://localhost:9001/api/admin/menu",
        contentType: "application/json",
        success: function (response) {
          let data = response.data;
          let renderDataMenu = ``;
          console.log(data);

          data.forEach(menu => {
            if (menu.parentId === null) {
              renderDataMenu += `
              <div class="form-check">
                <input class="form-check-input parent-checkbox" type="checkbox" value="${menu.id}" id="menuId-${menu.id}">
                <label class="form-check-label" for="menuId-${menu.id}">
                  ${menu.name}
                </label>
              </div>
            `}
            
            data.forEach(childMenu => {
              if (childMenu.parentId === menu.id) {
                renderDataMenu += `
                <div class="form-check ms-4">
                  <input class="form-check-input child-checkbox" type="checkbox" value="${childMenu.id}" data-parent-id="${menu.id}" id="menuId-${childMenu.id}">
                  <label class="form-check-label" for="menuId-${childMenu.id}">
                    ${childMenu.name}
                  </label>
                </div>
              `}
            })
          });

          
          $('#accessTree').html(renderDataMenu);

          $('#accessTree').on('change', '.form-check-input', function() {
            const parentId = $(this).val();
            const isChecked = $(this).is(':checked');

            $(`#accessTree .child-checkbox[data-parent-id="${parentId}"]`).prop('checked', isChecked);
          });

          $('#accessTree').on('change', '.child-checkbox', function() {
            const parentId = $(this).data('parent-id');

            if($(this).is(':checked')) {
              $(`#accessTree #menuId-${parentId}`).prop('checked', true);
            }else{
              const allUnchecked = $(`#accessTree .child-checkbox[data-parent-id="${parentId}"]:checked`).length === 0;
              if (allUnchecked) {
                $(`#accessTree #menuId-${parentId}`).prop('checked', false);
              }
            }
          });

          $.ajax({
            type: "get",
            url: `http://localhost:9001/api/transaction/menu-role/menuRoleAccess/${roleId}`,
            contentType: "application/json",
            success: function (accessResponse) {
              let accessedMenuIds = accessResponse.data; // Asumsikan respons berisi array ID menu
              console.log(accessedMenuIds);
              
              // Checklist menu yang sudah diakses berdasarkan response
              accessedMenuIds.forEach(accessedMenuId => {
                $(`#accessTree #menuId-${accessedMenuId.menuId}`).prop('checked', true);
              });

              // Update status checkbox select all
              const allChecked = $('#accessTree .form-check-input').length === $('#accessTree .form-check-input:checked').length;
              $('#selectAll').prop('checked', allChecked);
            },
            error: function (error) {
              console.log("Error loading menu access:", error);
            }
          });

          // untuk Mengecek semua checkbox
          $('#accessTree').on('change', '.form-check-input', function() {
            const allChecked = $('#accessTree .form-check-input').length === $('#accessTree .form-check-input:checked').length;
            $('#selectAll').prop('checked', allChecked);
          })
        }
      });

      $('#baseModalFooter').html(`
        <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
          Batal
        </button>
        <button id="saveMenuAccess" type="button" class="btn btn-primary">Simpan</button>
        `);
      
      $('#saveMenuAccess').on("click", function () {
        if ($('#accessTree .form-check-input:checked').length === 0) {
          if (!$('#accessTree-error').length) {
            $('#accessTree').after(`<p id="accessTree-error" class="text-danger">Minimal satu menu harus dipilih.</p>`);
          }
          $('#accessTree').css("border-color", "red");
        } else {
          $('#accessTree-error').remove();
          $('#accessTree').css("border-color", "");
          saveMenuAccess(roleId);
        }
      });
    },
    error: function (error) {
      console.log(error);
    }
  });

  function saveMenuAccess(roleId) {
    let menuNames = [];
    $('#accessTree .form-check-input:checked').each(function () {
      menuNames.push($(this).next('label').text());
    });

    let menuIds = [];
    $('#accessTree .form-check-input:checked').each(function () {
      menuIds.push($(this).val());
    });

    $.ajax({
      type: "get",
      url: "/set-menu-access/modal",
      contentType: "html",
      success: function (modal) {
        $('#baseModal').modal('show');
        $('#baseModalTitle').html('<strong class="fs-2">Konfirmasi</strong>');
        $('#baseModalBody').html(modal);
        $('#modalKonfirmasi').html(`<p class="fs-4">Anda akan memberikan akses menu berikut:</p><p>${menuNames.join(', ')}</p>`);
        $('#baseModalFooter').html(`
          <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
            Batal
          </button>
          <button id="submitMenuAccess" type="button" class="btn btn-primary">Simpan</button>
        `);

        $('#submitMenuAccess').off('click').on('click', function () {
          $.ajax({
            type: "POST",
            url: `http://localhost:9001/api/transaction/menu-role/save-menu-access/${roleId}`,
            data: JSON.stringify(menuIds),
            contentType: "application/json",
            success: function (response) {
              $('#baseModalTitle').html(`<strong class="fs-2">Sukses</strong>`);
              $('#baseModalBody').html(`<p class="text-center fs-3">Akses menu berhasil Atur</p>`);
              $('#baseModalFooter').html("");
            },
            error: function (error) {
              console.log(error);
            },
            complete: function () {
              $('#baseModal').on('hidden.bs.modal', function () {
                window.location.reload();
              });
            }
          });
        });
      }
    });
  }
}


    


