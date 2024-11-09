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

  dataObj.forEach((role) => {
    tableContainer.append(`
            <tr data-id="${role.id}">
                <td>${role.name}</td>
                <td>${role.code}</td>
                <td>
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

function renderPlaceholder() {
  $("#role-table").html(`
    <tr class="placeholder-glow">
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
    </tr>
    <tr class="placeholder-glow">
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
    </tr>
    <tr class="placeholder-glow">
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
    </tr>
    <tr class="placeholder-glow">
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
    </tr>
    <tr class="placeholder-glow">
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
      <td><span class="placeholder col-8"></span></td>
    </tr>
        `);
}

function renderPagination(pageInfo) {
  $("#paginationContainer").html("");

  if (pageInfo.totalPages >= 3) {
    $("#paginationContainer").append(`
    <li class="page-item prev ${0 === pageInfo.page ? "disabled" : ""}">
        <button class="page-link nav-btn" data-page="0"><i
         class="tf-icon bx bx-chevrons-left"></i></button>
    </li>
        `);
  }

  for (let i = 0; i < pageInfo.totalPages; i++) {
    $("#paginationContainer").append(`
        <li class="page-item ${i === pageInfo.page ? "active disabled" : ""}">
            <button class="page-link nav-btn" data-page="${i}">
              ${"<span>" + (i + 1) + "</span>"}
            </button>
        </li>
        `);
  }

  if (pageInfo.totalPages >= 3) {
    $("#paginationContainer").append(`
    <li class="page-item prev ${
      pageInfo.totalPages - 1 === pageInfo.page ? "disabled" : ""
    }">
        <button class="page-link nav-btn" data-page="${
          pageInfo.totalPages - 1
        }"><i
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

let debounceTimer;
$("#searchRole").on("input", function () {
  const query = $(this).val().trim();

  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    if (query != searchQuery) {
      searchQuery = query;
      loadSearchResult(0, rowPerPage, sortDirection, searchQuery);
    }
  }, 400);
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

$(document).on("input", ".save-role", function () {
  const roleCodeFilled = $("#roleCode").val().trim() !== "";
  const roleNameFilled = $("#roleName").val().trim() !== "";

  if (roleCodeFilled && roleNameFilled) {
    $("#saveRoleBtn").removeClass("disabled");
    $("#updateRoleBtn").removeClass("disabled");
  } else {
    $("#saveRoleBtn").addClass("disabled");
    $("#updateRoleBtn").removeClass("disabled");
  }
});
