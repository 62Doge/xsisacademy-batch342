function loadMedicalItemModal() {
  $("#baseModalBody").empty();
  $("#baseModalTitle").empty();
  $("#baseModalFooter").empty();

  $("#baseModalTitle").html(
    `<strong class="text-primary">Cari Obat & Alat Kesehatan</strong>`
  );
  $("#baseModalBody").append(`
      <p class="text-secondary">Masukkan minimal 1 kata kunci untuk pencarian anda</p>
      <form id="searchMedicalItemForm"> 
          <div class="mb-3">
              <label class="form-label" for="medicalItemCategoryName">Kategori</label>
              <select class="form-select" name="categoryName" id="medicalItemCategoryName">
              </select>
              <span id="medicalItemCategoryNameError" class="text-danger"></span>
          </div>
          <div class="mb-3">
              <label class="form-label" for="medicalItemKeyword">Kata Kunci</label>
              <input name="keyword" type="text" class="form-control" id="medicalItemKeyword" placeholder="Nama obat atau indikasi" >
              <span id="medicalItemKeywordError" class="text-danger"></span>
          </div>
          <div class="mb-3">
            <a class="" href="javascript:void(0);">
                <div class="form-check">
                    <label class="form-check-label w-100" for="otcMedicalItem">
                        <input type="radio" class="form-check-input" name="segmentationName"
                            id="otcMedicalItem" value="bebas" checked>
                        Hanya cari obat bebas (tanpa resep)
                    </label>
                </div>
            </a>
          </div>
          <div class="mb-3">
            <a class="" href="javascript:void(0);">
                <div class="form-check">
                    <label class="form-check-label w-100" for="prescriptionOnlyMedicalItem">
                        <input type="radio" class="form-check-input" name="segmentationName"
                            id="prescriptionOnlyMedicalItem" value="">
                        Cari semua obat (termasuk obat keras)
                    </label>
                </div>
            </a>
          </div>
          <div class="mb-3">
              <label class="form-label" >Harga</label>
              <div class="row">
                  <div class="col">
                      <input onchange="checkNumber(this)" onkeyup="checkNumber(this)" name="minPrice" type="number" min=0 class="form-control" id="minPrice" placeholder="Rp 0">
                  </div>
                  <div class="col-auto d-flex align-items-center">
                      sampai
                  </div>
                  <div class="col">
                      <input onchange="checkNumber(this)" onkeyup="checkNumber(this)" name="maxPrice" type="number" min=0 class="form-control" id="maxPrice" placeholder="Rp 100.000.000">
                  </div>
              </div>
              <span id="priceRangeError" class="text-danger"></span>
          </div>
      </form>
  `);
  $("#baseModalFooter").html(`
  <button id="resetMedicalItemSearch" onclick="loadMedicalItemModal()" type="button" class="btn btn-outline-primary">
  Atur Ulang
  </button>
  <button id="searchMedicalItemSearch" type="button" class="btn btn-primary">Cari</button>
  `);

  loadMedicalItemCategorySelect();
  document
    .getElementById("searchMedicalItemSearch")
    .addEventListener("click", function (event) {
      validateForm(event);
    });
  $("#searchMedicalItemForm").submit(function (event) {
    validateForm(event);
  });
}

function validateForm(event) {
  event.preventDefault();
  let isValid = true;

  // Check each required field
  const medicalItemKeyword = document.getElementById("medicalItemKeyword");
  const medicalItemKeywordError = document.getElementById(
    "medicalItemKeywordError"
  );
  const minPrice = document.getElementById("minPrice");
  const maxPrice = document.getElementById("maxPrice");
  const priceRangeError = document.getElementById("priceRangeError");

  // Clear previous errors
  medicalItemKeyword.textContent = "";
  medicalItemKeywordError.textContent = "";
  minPrice.textContent = "";
  maxPrice.textContent = "";
  priceRangeError.textContent = "";

  // Check Medical item keyword
  if (!medicalItemKeyword.value.trim()) {
    medicalItemKeyword.classList.add("is-invalid");
    medicalItemKeywordError.textContent = "Masukkan minimal 1 kata kunci!";
    isValid = false;
  } else {
    medicalItemKeyword.classList.remove("is-invalid");
  }
  // Check price range
  console.log(minPrice.value, maxPrice.value);
  if (maxPrice.value - minPrice.value < 0) {
    maxPrice.classList.add("is-invalid");
    minPrice.classList.add("is-invalid");
    priceRangeError.textContent =
      "Maksimum harga tidak bisa lebih kecil dari minimum!";
    isValid = false;
  } else {
    minPrice.classList.remove("is-invalid");
    maxPrice.classList.remove("is-invalid");
  }
  if (isValid) {
    // If all fields are valid, submit the form
    // event.target.submit();
    processSearch();
  }
}

function processSearch() {
  const formData = new FormData(
    document.getElementById("searchMedicalItemForm")
  );
  const params = new URLSearchParams(formData).toString();
  window.location.href = `/medical-item?${params}`;
}

function checkNumber(numberElement) {
  if (numberElement.value < 0) {
    numberElement.value = 0;
  }
}

function loadMedicalItemCategorySelect() {
  $.ajax({
    type: "GET",
    url: "http://localhost:9001/api/admin/medical-item-category",
    contentType: "application/json",
    success: function (response) {
      const medicalItemCategory = response.data;

      $("#medicalItemCategoryName").empty();
      $("#medicalItemCategoryName").append(
        `<option value="" selected>-- Pilih --</option>`
      );
      $.each(medicalItemCategory, function (index, category) {
        $("#medicalItemCategoryName").append(
          `<option value=${category.name} >${category.name}</option>`
        );
      });
    },
    error: function (error) {
      console.error("Failed to load location data:", error);
    },
  });
}
