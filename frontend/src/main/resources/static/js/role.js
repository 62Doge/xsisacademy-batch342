$(document).ready(function () {
  loadSearchResult(0, rowPerPage, sortDirection);

  // make checbox checked when input focus
  $("#customRowPerPageInputNumber").on("focus", () => {
    $("#customRowPerPageCheckbox").prop("checked", true);
  });
});

const ROLE_URL = BE_BASE_URL + "/admin/role";
let sortDirection = "asc";
let rowPerPage = 5;
let currentPage = 0;
let searchQuery = "";

function renderTableData(dataObj) {
  const tableContainer = $("#role-table");
  tableContainer.html("");

  if (dataObj.length < 1) {
    tableContainer.append(`
       <tr>
       <td colspan="3" class="text-center fs-4">Data tidak ditemukan</td>
       </tr>
      `);

  } else {
    dataObj.forEach((role) => {
      tableContainer.append(`
              <tr data-id="${role.id}">
                  <td>${role.name}</td>
                  <td>${role.code}</td>
                  <td class="text-center">
                      <button type="button" class="btn btn-icon btn-outline-warning editRoleButton">
                          <span class="tf-icons bx bxs-edit"></span>
                      </button>
                      <button type="button" class="btn btn-icon btn-outline-danger deleteButton">
                          <span class="tf-icons bx bxs-trash"></span>
                      </button>
                  </td>
              </tr>
                  `);
    });
  }
}

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
  $("#paginationContainer").html("");
  const totalPages = pageInfo.totalPages;
  const currentPage = pageInfo.page;

  $("#paginationContainer").append(`
    <li class="page-item disabled d-flex align-items-center justify-content-center" style="margin-right: 10px;">
      <span class="page-info">
        Halaman ${currentPage + 1} dari ${totalPages}
      </span>
    </li>
  `);

  if (totalPages > 3) {
    $("#paginationContainer").append(`
    <li class="page-item prev ${0 === currentPage ? "disabled" : ""}">
        <button class="page-link nav-btn" data-page="0"><i
         class="tf-icon bx bx-chevrons-left"></i></button>
    </li>
        `);
  }

  let startPage = Math.max(0, currentPage - 1);
  let endPage = Math.min(totalPages - 1, currentPage + 1);

  if (currentPage === 0) {
    endPage = Math.min(totalPages - 1, 2);
  } else if (currentPage === totalPages - 1) {
    startPage = Math.max(0, totalPages - 3);
  }

  for (let i = startPage; i <= endPage; i++) {
    $("#paginationContainer").append(`
      <li class="page-item ${i === currentPage ? "active disabled" : ""}">
        <button class="page-link nav-btn" data-page="${i}">
          ${i + 1}
        </button>
      </li>
    `);
  }

  if (totalPages > 3) {
    $("#paginationContainer").append(`
    <li class="page-item prev ${totalPages - 1 === currentPage ? "disabled" : ""
      }">
        <button class="page-link nav-btn" data-page="${totalPages - 1}"><i
         class="tf-icon bx bx-chevrons-right"></i></button>
    </li>
        `);
  }
}

function loadSearchResult(
  pageNo = 0,
  pageSize = 5,
  sortDirection = "asc",
  searchText = ""
) {
  renderPlaceholder();
  const sortBy = "name";

  $.ajax({
    type: "get",
    url: ROLE_URL,
    data: {
      pageNo,
      pageSize,
      sortBy,
      sortDirection,
      searchText,
    },
    dataType: "json",
    success(response) {
      renderPagination(response.data.metadata);
      setTimeout(() => {
        renderTableData(response.data.content);
      }, 500);
    },
    error() {
      $("#errorModal").modal("show");
    },
  });
}

$(document).on("click", ".page-link.nav-btn", function () {
  const page = $(this).data("page");
  currentPage = page;
  loadSearchResult(page, rowPerPage, sortDirection, searchQuery);
});

$("#applySorting").click(function (e) {
  e.preventDefault();
  const selectedSort = $('input[name="orderTypeRadio"]:checked').data("sort");
  if (selectedSort != sortDirection) {
    sortDirection = selectedSort;
    loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
  }
});

$("#customRowPerPageInputNumber").on("input", function (e) {
  e.preventDefault();
  let inputValue = parseInt($(this).val());
  if (inputValue < 1) {
    $(this).val(1);
  }
  $("#customRowPerPageCheckbox").data("page", inputValue);
});

$("#applyRowPerPage").click(function (e) {
  e.preventDefault();
  const selectedPerPageVal = $('input[name="row-per-page"]:checked').data(
    "page"
  );

  if (selectedPerPageVal != rowPerPage) {
    rowPerPage = selectedPerPageVal;
    loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
  }
});

function showSearchConfirmation(query) {
  $("#deleteConfirmationModalBody").html(`
    <p class="pb-0 mb-2 fs-5">Apakah anda yakin akan mengubah mencari data dengan query <span class="fw-bold">${query}</span>?:</p>
    `);

  $("#deleteConfirmationModalButton").data("mode", "search");
  $("#deleteConfirmationModalButton").text("Ya, cari");

  $("#deleteConfirmationModal").modal("show");
}

let debounceTimer;
$("#searchRole").on("input", function () {
  const query = $(this).val().trim();

  // clearTimeout(debounceTimer);
  // debounceTimer = setTimeout(() => {
  if (query != searchQuery && query.length % 4 == 0 && query.length != 0) {
    searchQuery = query;
    // loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
    showSearchConfirmation(searchQuery);

    // show confirmation modal

  } else if (query.length == 0) {
    searchQuery = query;
    loadSearchResult(0, rowPerPage, sortDirection, searchQuery);

  }
  // }, 400);
});

function setBaseModal(modaltitle = "", modalBody = "", modalFooter = "") {
  $("#baseModalTitle").html(modaltitle);
  $("#baseModalBody").html(modalBody);
  $("#baseModalFooter").html(modalFooter);
}

function showBaseModal() {
  $("#baseModal").modal("show");
}

function setAndShowSuccessModal(modalBody) {
  $("#successModalBody").html(modalBody);
  $("#successModal").modal("show");
}

// $(document).on("input", ".save-role", function () {
//   const roleCodeFilled = $("#roleCode").val().trim() !== "";
//   const roleNameFilled = $("#roleName").val().trim() !== "";

//   if (roleCodeFilled && roleNameFilled) {
//     $("#saveRoleBtn").removeClass("disabled");
//     $("#updateRoleBtn").removeClass("disabled");
//   } else {
//     $("#saveRoleBtn").addClass("disabled");
//     $("#updateRoleBtn").removeClass("disabled");
//   }
// });
