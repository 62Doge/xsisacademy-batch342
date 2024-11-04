let locationLevelData = [];
let locationData = [];

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
      populateLocationLevelSelect(locationLevelResponse.data.content);
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
      populateLocationSelect(locationData);
    },
  }).responseText;
}

function loadParentByLevel(selectId = null) {
  let locationLevelName = $("#levelLocationId :selected").text();
  $("#parentId").empty();
  $("#parentId").append(
    `<option value="" selected disabled hidden>Choose here</option>`
  );
  if (locationLevelName == "Provinsi") {
    $("#parentId").append(
      `<option value="" selected>Buat Provinsi Baru</option>`
    );
  } else {
    $.each(locationData, function (index, location) {
      if (
        locationLevelName === "kota" ||
        locationLevelName === "kabupaten" ||
        locationLevelName === "Kota" ||
        locationLevelName === "Kabupaten"
      ) {
        if (location.parent === null) {
          const isSelected = selectId === location.id ? "selected" : "";
          $("#parentId").append(
            `<option value=${location.id} ${isSelected}> ${location.name}</option>`
          );
        }
      } else {
        let locLevelOne = location.name;
        let locLevelTwo = "";
        if (location.parent !== null) {
          locLevelTwo =
            location.parent !== null ? ", " + location.parent.name : "";
        } else {
          return;
        }
        let loc = `${locLevelOne}${locLevelTwo}`;
        const isSelected = selectId === location.id ? "selected" : "";
        $("#parentId").append(
          `<option value=${location.id} ${isSelected}> ${loc}</option>`
        );
      }
    });
  }
}

function populateLocationSelect(locationContent, selectId = null) {
  $("#parentId").empty();
  $("#parentId").append(
    `<option value="" selected disabled hidden>Choose here</option>`
  );
  $.each(locationContent, function (index, location) {
    let locLevelOne = location.name;
    let locLevelTwo = "";
    if (location.parent !== null) {
      locLevelTwo = location.parent !== null ? ", " + location.parent.name : "";
    }
    let loc = `${locLevelOne}${locLevelTwo}`;
    const isSelected = selectId === location.id ? "selected" : "";
    $("#parentId").append(
      `<option value=${location.id} ${isSelected}> ${loc}</option>`
    );
  });
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
      let locationData = response.data;
      let tableData = ``;

      locationData.forEach((location) => {
        let locLevelOne = "";
        let locLevelTwo = "";
        if (location.parent !== null) {
          locLevelOne = location.parent.name;
          locLevelTwo =
            location.parent.parent !== null
              ? ", " + location.parent.parent.name
              : "";
        }
        let loc = `${locLevelOne}${locLevelTwo}`;
        console.log(location.parent);
        if (location.parent !== null) {
          console.log(location.parent.parent);
        }
        tableData += `
                  <tr>
                    <td>${location.name}</td>
                    <td>${location.locationLevel.name}</td>
                    <td>${loc}</td>
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
        let locLevelTwo = "";
        if (location.parent !== null) {
          locLevelOne = location.parent.name;
          locLevelTwo =
            location.parent.parent !== null
              ? ", " + location.parent.parent.name
              : "";
        }
        let loc = `${locLevelOne}${locLevelTwo}`;
        console.log(location.parent);
        if (location.parent !== null) {
          console.log(location.parent.parent);
        }
        tableData += `
                  <tr>
                    <td>${location.name}</td>
                    <td>${location.locationLevel.name}</td>
                    <td>${loc}</td>
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
      loadLocationLevel();
      loadParent();
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
      console.error(error);
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
          $("#baseModal").modal("show");
          $("#baseModalTitle").html(`<strong>Edit Lokasi</strong>`);
          $("#baseModalBody").html(editForm);

          $("#locationName").val(location.name);
          $("#levelLocationId").val(location.locationLevelId);

          $("#baseModalFooter").html(`
                        <button data-bs-dismiss="modal" type="button" class="btn btn-warning">
                            Batal
                        </button>
                        <button onclick="updateLocation(${id})" type="button" class="btn btn-primary">
                            Simpan
                        </button>
                    `);
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
      console.error("Failed to delete location:", error);
    },
  });
}
