let locationLevelData = [];
let locationData = [];
let currentPage = 1;
let pageSize = 5;
let sortBy = "id";
let sortDir = "asc";
let totalPages;
let currentSearchQuery;
let currentLocationLevelId;

document.addEventListener("DOMContentLoaded", (event) => {
  loadParent();
  loadLocationLevel();
});

$(document).ready(function () {
  loadLocations();

  $("#searchLocation").on("input", function () {
    currentSearchQuery = $(this).val();
    $("#location-table").empty();
    loadLocations();
  });
});

function loadLocations() {
  let url = "";
  if (currentLocationLevelId) url += `/level/${currentLocationLevelId}`;
  if (currentSearchQuery) url += `/name/${currentSearchQuery}`;
  $.ajax({
    type: "GET",
    url: `http://localhost:9001/api/admin/location${url}?pageNo=${
      currentPage - 1
    }&pageSize=${pageSize}&sortBy=${sortBy}&sortDirection=${sortDir}`,
    contentType: "application/json",
    success: function (response) {
      let locationData = response.data.content;
      totalPages = response.data.metadata.totalPages;

      let tableData = ``;
      locationData.forEach((location) => {
        let locLevelOne = "";
        if (location.parent !== null) {
          locLevelOne = `${location.parent.locationLevel.abbreviation} ${location.parent.name}`;
        }
        tableData += `
                  <tr>
                    <td>${location.name}</td>
                    <td>${location.locationLevel.name}</td>
                    <td>${locLevelOne}</td>
                    <td>
                        <button onclick="openEditForm(${location.id})" type="button" class="btn btn-icon btn-outline-warning">
                            <span class="tf-icons bx bxs-edit"></span>
                        </button>
                        <button onclick="openDeleteModal(${location.id})" type="button" class="btn btn-icon btn-outline-danger">
                            <span class="tf-icons bx bxs-trash"></span>
                        </button>
                    </td>
                  </tr>
                `;
      });
      $("#location-table").html(tableData);
      pageButtons();
    },
    error: function (error) {
      alert("Failed to load locations by location level!");
      console.error("Error loading locations by location level:", error);
    },
  });
}

function pageButtons() {
  // console.log(`${totalPages} : curr ${currentPage}`);
  if (currentPage > totalPages) {
    if (totalPages > 0) {
      moveToPage(totalPages);
      return;
    } else {
      currentPage = 1;
    }
  }
  // Show pages
  $("#pageList").empty();

  // Previous button
  $("#pageList").append(`
      <li class="page-item prev" id="previousPageControl">
          <a class="page-link" href="javascript:previousPage();"><i class='bx bx-chevron-left'></i></a>
      </li>
  `);

  // Determine start and end of page range
  let startPage = Math.max(1, currentPage);
  let endPage = Math.min(totalPages, currentPage);

  // If current page is greater than 3, show first page and '...'
  if (currentPage > 1) {
    $("#pageList").append(`
        <li class="page-item">
            <a class="page-link" href="javascript:moveToPage(1);">1</a>
        </li>
        <li class="page-item">
            <a class="page-link" href="javascript:openPageModal();">...</a>
        </li>
    `);
  }

  // Loop through the visible page buttons
  for (let pageNum = startPage; pageNum <= endPage; pageNum++) {
    if (pageNum === currentPage) {
      $("#pageList").append(`
          <li class="page-item active">
              <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
          </li>
      `);
    } else {
      $("#pageList").append(`
          <li class="page-item">
              <a class="page-link" href="javascript:moveToPage(${pageNum});">${pageNum}</a>
          </li>
      `);
    }
  }

  // If there are more pages after the current range, show '...' and last page
  if (currentPage < totalPages) {
    $("#pageList").append(`
        <li class="page-item">
            <a class="page-link" href="javascript:openPageModal();">...</a>
        </li>
        <li class="page-item">
            <a class="page-link" href="javascript:moveToPage(${totalPages});">${totalPages}</a>
        </li>
    `);
  }

  // Next button
  $("#pageList").append(`
      <li class="page-item next" id="nextPageControl">
          <a class="page-link" href="javascript:nextPage();"><i class='bx bx-chevron-right'></i></a>
      </li>
  `);

  // Set the selected sorting options in the dropdown
  $('input[name="orderColumnRadio"][value="' + sortBy + '"]').prop(
    "checked",
    true
  );
  $('input[name="orderTypeRadio"][value="' + sortDir + '"]').prop(
    "checked",
    true
  );
}

// Function to open a modal for navigating to a specific page
function openPageModal() {
  // Set up the content for the "Go to Page" modal
  document.getElementById("baseModalTitle").innerText = "Go to Page";

  // Modal body: input field for the page number
  document.getElementById("baseModalBody").innerHTML = `
    <input type="number" id="pageInput" class="form-control" placeholder="Enter page number" min="1" max="${totalPages}">
  `;

  // Modal footer: close and go buttons
  document.getElementById("baseModalFooter").innerHTML = `
    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
    <button type="button" class="btn btn-primary" onclick="goToPage()">Go</button>
  `;

  // Show the modal
  $("#baseModal").modal("show");
}

function goToPage() {
  const pageInput = document.getElementById("pageInput");
  pageNumber = parseInt(pageInput.value);

  if (pageNumber >= 1 && pageNumber <= totalPages) {
    moveToPage(pageNumber); // Call your existing function to navigate to the specified page
  } else {
    alert("Please enter a valid page number.");
  }
  $("#baseModal").modal("hide");
  $(".modal-backdrop").remove();
}

function moveToPage(pageNumber) {
  currentPage = pageNumber;
  loadLocations();
}

function nextPage() {
  if (currentPage + 1 <= totalPages) {
    currentPage++;
  }
  loadLocations();
}

function previousPage() {
  if (currentPage - 1 > 0) {
    currentPage--;
  }
  loadLocations();
}

function loadLocationLevel() {
  $.ajax({
    type: "get",
    url: "http://localhost:9001/api/admin/location-level?pageSize=200",
    contentType: "application/json",
    success: function (locationLevelResponse) {
      locationLevelData = locationLevelResponse.data.content;
      populateLocationLevelDropdown();
    },
  }).responseText;
}

function loadParent() {
  $.ajax({
    type: "get",
    url: "http://localhost:9001/api/admin/location?pageSize=200",
    contentType: "application/json",
    success: function (locationResponse) {
      locationData = locationResponse.data.content;
    },
  }).responseText;
}

function populateParentByLevel(selectId = null, locationId = null) {
  let locationLevelName = $("#levelLocationId :selected").text().toLowerCase();
  $("#parentId").empty();
  $("#parentId").append(
    `<option value="">Choose here</option>`
  );
  if (locationLevelName === "provinsi") {
    $("#parentId").append(
      `<option value="" selected>Buat Provinsi Baru</option>`
    );
  } else {
    $.each(locationData, function (index, location) {
      let loc = "";
      let tempLocation = location;
      while (tempLocation.parent !== null) {
        loc += `${tempLocation.locationLevel.abbreviation} ${tempLocation.name}, `;
        tempLocation = tempLocation.parent;
      }
      loc += `${tempLocation.locationLevel.abbreviation} ${tempLocation.name}`;
      const isSelected = selectId === location.id ? "selected" : "";
      const secondLevelLocationCheck =
        (locationLevelName === "kota" || locationLevelName === "kabupaten") &&
        location.parentId !== null;
      const upperLevelLocationCheck =
        locationLevelName !== "kota" &&
        locationLevelName !== "kabupaten" &&
        location.parentId === null;
      const sameLevelLocationCheck =
        locationLevelName == location.locationLevel.name.toLowerCase();
      const isHidden =
        secondLevelLocationCheck ||
        upperLevelLocationCheck ||
        sameLevelLocationCheck || 
        locationId === location.id
          ? "hidden"
          : "";
      $("#parentId").append(
        `<option value=${location.id} ${isSelected} ${isHidden}> ${loc}</option>`
      );
    });
  }
}

function populateLocationLevelSelect(locationLevelContent, selectId = null) {
  $("#levelLocationId").empty();
  $("#levelLocationId").append(
    `<option value="" selected>Choose here</option>`
  );

  $.each(locationLevelContent, function (index, levelLocation) {
    const isSelected = selectId === levelLocation.id ? "selected" : "";
    $("#levelLocationId").append(
      `<option value=${levelLocation.id} ${isSelected}>${levelLocation.name}</option>`
    );
  });
}

// Function to populate the dropdown menu
function populateLocationLevelDropdown() {
  // console.log(locationLevelData);
  const dropdown = document.getElementById("location-level-dropdown");
  dropdown.innerHTML = ""; // Clear any existing content

  // Create a form to wrap radio button items (optional, for better grouping)
  const form = document.createElement("form");

  // Add the "None" option with a radio button
  const noneItem = document.createElement("li");
  const noneLabel = document.createElement("label");
  noneLabel.className = "dropdown-item d-flex align-items-center";

  const noneRadio = document.createElement("input");
  noneRadio.type = "radio";
  noneRadio.name = "locationLevel";
  noneRadio.value = "none";
  noneRadio.onclick = function () {
    currentLocationLevelId = null;
    loadLocations();
  }; // Reset function on click
  noneRadio.className = "me-2";

  noneLabel.appendChild(noneRadio);
  noneLabel.appendChild(document.createTextNode("None"));
  noneItem.appendChild(noneLabel);
  form.appendChild(noneItem);

  // Add the location level items dynamically with radio buttons
  locationLevelData.forEach((level) => {
    const listItem = document.createElement("li");
    const label = document.createElement("label");
    label.className = "dropdown-item d-flex align-items-center";

    const radio = document.createElement("input");
    radio.type = "radio";
    radio.name = "locationLevel";
    radio.value = level.id;
    radio.onclick = function () {
      currentLocationLevelId = level.id;
      loadLocations();
    };
    radio.className = "me-2"; // Adds some margin to the right

    label.appendChild(radio);
    label.appendChild(document.createTextNode(level.name));
    listItem.appendChild(label);
    form.appendChild(listItem);
  });

  dropdown.appendChild(form);
}

function validateAddForm(event, updateId = null) {
  event.preventDefault();
  let isValid = true;

  // Check each required field
  const levelLocationId = document.getElementById("levelLocationId");
  const levelLocationIdError = document.getElementById("levelLocationIdError");
  const locationName = document.getElementById("locationName");
  const locationNameError = document.getElementById("locationNameError");

  // Clear previous errors
  locationNameError.textContent = "";
  levelLocationIdError.textContent = "";

  // Check Level Location ID
  if (!levelLocationId.value) {
    levelLocationId.classList.add("is-invalid");
    levelLocationIdError.textContent = "Level Lokasi is required";
    isValid = false;
  } else {
    levelLocationId.classList.remove("is-invalid");
  }
  // Check Location Name
  if (!locationName.value.trim()) {
    locationName.classList.add("is-invalid");
    locationNameError.textContent = "Nama Lokasi is required";
    isValid = false;
  } else {
    locationName.classList.remove("is-invalid");
  }
  if (isValid) {
    // If all fields are valid, submit the form
    // event.target.submit();
    if (updateId) {
      updateLocation(updateId);
    } else {
      saveLocation();
    }
  }
}

function openAddForm() {
  $.ajax({
    type: "get",
    url: "/location/addForm",
    contentType: "html",
    success: function (addForm) {
      $("#baseModalBody").empty();
      $("#baseModal").modal("show");
      $("#baseModalTitle").html(`<strong>Tambah Lokasi</strong>`);
      $("#baseModalBody").append(addForm);
      $("#baseModalFooter").html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning" data-bs-dismiss="modal">
                    Batal
                </button>
                <button id="saveLocationBtn" type="button" class="btn btn-primary">Simpan</button>
            `);
      document
        .getElementById("saveLocationBtn")
        .addEventListener("click", function (event) {
          validateAddForm(event);
        });
      $("#locationForm").submit(function (event) {
        validateAddForm(event);
      });
      populateLocationLevelSelect(locationLevelData);
    },
  });
}

function saveLocation() {
  let name = $("#locationName").val().trim();
  let locationLevelId = parseInt($("#levelLocationId").val());
  let parentId = parseInt($("#parentId").val());
  let createdBy = 0; //If user logged in works, use session user logged in info

  let jsonData = { name, locationLevelId, parentId, createdBy };
  $.ajax({
    type: "POST",
    url: "http://localhost:9001/api/admin/location",
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    success: function (response) {
      $("#baseModal").modal("hide");
      loadParent();
      loadLocations();
    },
    error: function (error) {
      // if (error.status === 409)
      alert("Failed to save location: location conflict / already exist");
      console.error("Failed to save location: ", error);
    },
  });
}

function openEditForm(id) {
  $.ajax({
    type: "GET",
    url: `http://localhost:9001/api/admin/location/${id}`,
    contentType: "application/json",
    success: function (response) {
      let location = response.data;

      // load modal
      $.ajax({
        type: "get",
        url: "/location/editForm",
        contentType: "html",
        success: function (editForm) {
          $("#baseModalBody").empty();
          $("#baseModal").modal("show");
          $("#baseModalTitle").html(`<strong>Edit Lokasi</strong>`);
          $("#baseModalBody").append(editForm);
          $("#locationName").val(location.name);
          $("#levelLocationId").change(function() {
            populateParentByLevel(null, location.id)
          });
          $("#baseModalFooter").html(`
                        <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                            Batal
                        </button>
                        <button id="updateLocationBtn" type="button" class="btn btn-primary">
                            Simpan
                        </button>
                    `);
          document
            .getElementById("updateLocationBtn")
            .addEventListener("click", function (event) {
              validateAddForm(event, id);
            });
          $("#locationForm").submit(function (event) {
            validateAddForm(event, id);
          });
          populateLocationLevelSelect(
            locationLevelData,
            location.locationLevelId
          );
          populateParentByLevel(location.parentId);
        },
      });
    },
    error: function (error) {
      if (error.status === 409) alert("Failed to load location data!");
      console.error("Failed to load location data:", error);
    },
  });
}

function updateLocation(id) {
  let name = $("#locationName").val();
  let locationLevelId = $("#levelLocationId").val();
  let parentId = $("#parentId").val();
  let modifiedBy = ADMIN_LOGGED_ID;

  let jsonData = { name, locationLevelId, parentId, modifiedBy };
  $.ajax({
    type: "PUT",
    url: `http://localhost:9001/api/admin/location/update/${id}`,
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    success: function (response) {
      $("#baseModal").modal("hide");
      loadParent();
      loadLocations();
    },
    error: function (error) {
      alert("Failed to update location: location conflict / already exist");
      console.error("Failed to update location:", error);
    },
  });
}

function openDeleteModal(id) {
  $.ajax({
    type: "GET",
    url: `http://localhost:9001/api/admin/location/${id}`,
    contentType: "application/json",
    success: function (response) {
      let location = response.data;

      $("#baseModal").modal("show");
      $("#baseModalTitle").html(`<strong>Hapus Lokasi</strong>`);
      $("#baseModalBody").html(`
                <div>
                    Anda yakin akan menghapus Lokasi <span id="locationName">'${location.name}'</span>?
                </div>
            `);

      $("#baseModalFooter").html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button onclick="deleteLocation(${id},'${location.name}')" type="button" class="btn btn-danger">
                    Ya
                </button>
            `);
    },
    error: function (error) {
      alert("Failed to load location data for deletion!");
      console.error("Failed to load location data for deletion:", error);
    },
  });
}

function deleteLocation(id, locationName = null) {
  let jsonData = {
    deletedBy : ADMIN_LOGGED_ID
  };
  $.ajax({
    type: "PATCH",
    url: `http://localhost:9001/api/admin/location/soft-delete/${id}`,
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    success: function (response) {
      $("#baseModal").modal("hide");
      loadParent();
      loadLocations();
    },
    error: function (error) {
      if (error.status === 409) {
        // alert("Failed to delete: Location is used!");
        $("#baseModal").modal("show");
        $("#baseModalTitle").html(`<strong>Hapus Lokasi</strong>`);
        $("#baseModalBody").html(`
                  <div style="text-align: center;">
                      Tidak dapat menghapus Lokasi '<span id="locationName">${locationName}</span>'
                      <br>
                      Lokasi tersebut masih digunakan
                  </div>
              `);
        $("#baseModalFooter").html(`
                  <button data-bs-dismiss="modal" type="button" class="btn btn-primary">
                      Kembali
                  </button>
              `);
      }
      console.error(
        "Failed to delete location: location is used",
        error.status
      );
    },
  });
}
