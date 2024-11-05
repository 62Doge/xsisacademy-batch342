let locationLevelData = [];
let locationData = [];

document.addEventListener("DOMContentLoaded", (event) => {
  loadParent();
  loadLocationLevel();
});

$(document).ready(function () {
  loadData();

  $("#searchLocation").on("input", function () {
    const searchQuery = $(this).val();
    if (searchQuery) {
      searchLocation(searchQuery);
    } else {
      loadData();
    }
  });
});

function loadLocationLevel() {
  $.ajax({
    type: "get",
    url: "http://localhost:9001/api/admin/location-level?pageSize=200",
    contentType: "application/json",
    success: function (locationLevelResponse) {
      locationLevelData = locationLevelResponse.data.content;
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

// FIXME:  This function is not correct

function populateParentByLevel(selectId = null) {
  let locationLevelName = $("#levelLocationId :selected").text().toLowerCase();
  $("#parentId").empty();
  $("#parentId").append(
    `<option value="" selected disabled hidden>Choose here</option>`
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
      const isHidden =
        (locationLevelName === "kota" || locationLevelName === "kabupaten") &&
        location.parentId !== null
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
    `<option value="" selected disabled hidden>Choose here</option>`
  );

  $.each(locationLevelContent, function (index, levelLocation) {
    const isSelected = selectId === levelLocation.id ? "selected" : "";
    $("#levelLocationId").append(
      `<option value=${levelLocation.id} ${isSelected}>${levelLocation.name}</option>`
    );
  });
}

function searchLocation(name) {
  $.ajax({
    type: "GET",
    url: `http://localhost:9001/api/admin/location/name/${name}`,
    contentType: "application/json",
    success: function (response) {
      console.log(response);
      let locationData = response.data.content;
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
    },
    error: function (error) {
      console.error("Error searching locations:", error);
    },
  });
}

function loadData() {
  let tableData = ``;
  $.ajax({
    type: "get",
    url: "http://localhost:9001/api/admin/location?pageNo=0",
    contentType: "application/json",
    success: function (locationResponse) {
      console.log(locationResponse);
      // fixed routing to get content in Paging
      let locationData = locationResponse.data.content;

      locationData.forEach((location, index) => {
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
    },
    error: function (xhr, status, error) {
      console.error("Error loading data:", error);
    },
  });
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
        .addEventListener("click", function () {
          const form = document.getElementById("locationForm");
          if (form.checkValidity()) {
            saveLocation();
          } else {
            form.reportValidity();
          }
        });
      populateLocationLevelSelect(locationLevelData);
    },
  });
}

function saveLocation() {
  let name = $("#locationName").val();
  let locationLevelId = parseInt($("#levelLocationId").val());
  let parentId = parseInt($("#parentId").val());

  if (!name.trim()) {
    alert("Nama Lokasi harus diisi");
    return;
  }

  let jsonData = { name, locationLevelId, parentId };
  $.ajax({
    type: "POST",
    url: "http://localhost:9001/api/admin/location",
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    success: function (response) {
      location.reload();
    },
    error: function (error) {
      if (error.status === 409)
        alert("Failed to save location: location already exist");
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
            .addEventListener("click", function () {
              const form = document.getElementById("locationForm");
              if (form.checkValidity()) {
                updateLocation(id);
              } else {
                form.reportValidity();
              }
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
      console.error("Failed to load location data:", error);
    },
  });
}

function updateLocation(id) {
  let name = $("#locationName").val();
  let locationLevelId = $("#levelLocationId").val();
  let parentId = $("#parentId").val();

  if (!name.trim()) {
    alert("Nama Lokasi harus diisi");
    return;
  }

  let jsonData = { name, locationLevelId, parentId };
  $.ajax({
    type: "PUT",
    url: `http://localhost:9001/api/admin/location/update/${id}`,
    data: JSON.stringify(jsonData),
    contentType: "application/json",
    success: function (response) {
      $("#baseModal").modal("hide");
      loadData();
    },
    error: function (error) {
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
                    Anda akan menghapus <span id="locationName">${location.name}</span>?
                </div>
            `);

      $("#baseModalFooter").html(`
                <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                    Tidak
                </button>
                <button onclick="deleteLocation(${id})" type="button" class="btn btn-danger">
                    Ya
                </button>
            `);
    },
    error: function (error) {
      console.error("Failed to load location data for deletion:", error);
    },
  });
}

function deleteLocation(id) {
  $.ajax({
    type: "PATCH",
    url: `http://localhost:9001/api/admin/location/soft-delete/${id}`,
    success: function (response) {
      $("#baseModal").modal("hide");
      loadData();
    },
    error: function (error) {
      if (error.status === 409) {
        alert("Failed to delete: Location is used!");
      }
      console.error(
        "Failed to delete location: location is used",
        error.status
      );
    },
  });
}
