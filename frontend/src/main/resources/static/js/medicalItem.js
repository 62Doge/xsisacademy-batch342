let params;
let currentPage = 1;
let totalPages;
let pageSize;
let totalElements;
let medicalItems = [];
let cartItems = [];

document.addEventListener("DOMContentLoaded", (event) => {
  params = new URLSearchParams(window.location.search).toString();
  const paramObject = Object.fromEntries(
    new URLSearchParams(window.location.search)
  );
  if (!paramObject.keyword.length) {
    $("#baseModal").modal("show");
    $("#baseModalTitle").html(`<strong>Cari Obat & Alat Kesehatan</strong>`);
    $("#baseModalBody").html(`
                  <div style="text-align: center;">
                      Tidak dapat mencari obat dan alat kesehatan tanpa keyword.
                  </div>
              `);
    $("#baseModalFooter").html(`
                  <button data-bs-dismiss="modal" type="button" class="btn btn-primary">
                      Kembali
                  </button>
              `);

    return;
  }
  setResultText(paramObject);
  loadMedicalItems();
  if (!sessionStorage.getItem("cartItems")) saveCartToSessionStorage();
  loadCartFromSessionStorage();
});

function setResultText(paramObject) {
  let resultText = "Hasil Pencarian berdasarkan kata kunci: ";
  let firstParam = true;
  if (paramObject.categoryName.length) {
    firstParam = false;
    resultText += `Kategori ${paramObject.categoryName}`;
  }
  if (!firstParam) resultText += ", ";
  firstParam = false;
  if (paramObject.keyword.length) {
    resultText += `Kata kunci ${paramObject.keyword}`;
  }
  if (!firstParam) resultText += ", ";
  if (paramObject.segmentationName === "bebas") {
    resultText += "Hanya cari obat bebas(tanpa resep)";
  } else {
    resultText += "Cari semua obat (termasuk obat keras)";
  }
  $("#searchResultText").text(resultText);
}

function showSearchModal() {
  loadMedicalItemModal();
  $("#baseModal").modal("show");
}

function loadMedicalItems() {
  $.ajax({
    type: "GET",
    url: `http://localhost:9001/api/admin/medical-item/search/v2?${params}&pageNo=${
      currentPage - 1
    }`,
    success: function (response) {
      medicalItems = response.data.content;
      totalPages = response.data.metadata.totalPages;
      currentPage = response.data.metadata.page + 1;
      pageSize = response.data.metadata.size;
      totalElements = response.data.metadata.totalElements;
      populateMedicalItemCards();
    },
    error: function (xhr, status, error) {
      console.error(error);
    },
  });
}

function populateMedicalItemCards() {
  const container = document.getElementById("medicalItemCards");
  container.innerHTML = ""; // Clear existing content

  medicalItems.forEach((item) => {
    const card = document.createElement("div");
    card.className = "col";

    const cartItem = cartItems.find((cartItem) => cartItem.id == item.id);
    const quantity = cartItem ? cartItem.quantity : 0;

    card.innerHTML = `
          <div class="card h-100">
              <div class="card-body">
                <div>
                  <h6 class="card-title" style="font-size:1rem;font-weight:bold;color:#555;margin-bottom:0.2rem;">${
                    item.name
                  }</h6>
                  <p class="text-muted" style="font-size:0.875rem;color:#999;margin-bottom:0rem;margin-left:1rem;">${
                    item.packaging
                  }</p>
                  <p class="text-muted price-range" style="font-size:0.875rem;color:#999;margin-bottom:1rem;margin-left:1rem;">Rp ${item.priceMin.toLocaleString()} - Rp ${item.priceMax.toLocaleString()}</p>
                </div>
                <p class="card-text" style="font-size:0.875rem;color:#777;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;text-overflow:ellipsis;">${
                  item.indication
                }</p>
                </div>
              <div class="card-footer mt-0 pt-0 d-flex justify-content-between align-items-center">
                    ${
                      quantity > 0
                        ? `<button class="btn btn-outline-secondary quantity-btn" data-item-id="${item.id}" data-action="decrement">-</button>
                        <span class="quantity" data-item-id="${item.id}">${quantity}</span>
                        <button class="btn btn-outline-secondary quantity-btn" data-item-id="${item.id}" data-action="increment">+</button>`
                        : `<button class="btn btn-primary w-75 ms-2 add-to-cart" data-item-id="${item.id}">Tambah ke Keranjang</button>`
                    }
                  <button class="btn btn-outline-secondary">Detail</button>
              </div>
          </div>
      `;

    container.appendChild(card);
  });

  document.querySelectorAll(".add-to-cart").forEach((button) => {
    button.addEventListener("click", (event) => {
      const itemId = event.target.getAttribute("data-item-id");
      const item = medicalItems.find((item) => item.id == itemId);
      if (item && !cartItems.some((cartItem) => cartItem.id == itemId)) {
        updateCartItem(item, "increment");
      }
    });
  });

  document.querySelectorAll(".quantity-btn").forEach((button) => {
    button.addEventListener("click", (event) => {
      const itemId = event.target.getAttribute("data-item-id");
      const action = event.target.getAttribute("data-action");
      const item = medicalItems.find((item) => item.id == itemId);
      if (item) {
        updateCartItem(item, action);
      }
    });
  });

  const paginationContainer = document.getElementById("medicalItemPagination");
  let currentFirstData =
    totalElements != 0 ? (currentPage - 1) * pageSize + 1 : 0;
  let currentLastData =
    totalElements != 0 ? currentFirstData + medicalItems.length - 1 : 0;
  paginationContainer.innerHTML = `
      <p>Menampilkan ${currentFirstData} - ${currentLastData} dari ${totalElements}</p>
      <div id="pageList">
          <button id="prevBtn" onclick="previousPage()" class="btn btn-outline-secondary disabled" >Sebelumnya</button>
          <button id="nextBtn" onclick="nextPage()" class="btn btn-outline-secondary disabled" >Selanjutnya</button>
      </div>
  `;
  pageButtons();
}

// Save cart items to session storage
function saveCartToSessionStorage() {
  sessionStorage.setItem("cartItems", JSON.stringify(cartItems));
}

// Load cart items from session storage
function loadCartFromSessionStorage() {
  const savedCartItems = sessionStorage.getItem("cartItems");
  if (savedCartItems !== "[]") {
    cartItems = JSON.parse(savedCartItems);
    updateCartFooter();
  }
}

function updateCartItem(item, action) {
  const cartItem = cartItems.find((cartItem) => cartItem.id == item.id);

  if (action === "increment") {
    if (cartItem) {
      cartItem.quantity += 1;
    } else {
      cartItems.push({ ...item, quantity: 1 });
    }
  } else if (action === "decrement" && cartItem) {
    cartItem.quantity -= 1;
    if (cartItem.quantity <= 0) {
      cartItems = cartItems.filter((cartItem) => cartItem.id != item.id);
    }
  }

  saveCartToSessionStorage();
  updateCartFooter();
  populateMedicalItemCards(); // Update the UI
}

function updateCartFooter() {
  const totalItems = cartItems.length;
  const estimatedPrice = cartItems.reduce(
    (total, item) => total + item.quantity * item.priceMax,
    0
  );
  let htmlData = "";
  let watermark = new Date().getFullYear();
  if (totalItems) {
    htmlData = `
    <div class="container-xxl py-4 d-flex justify-content-between align-items-center" style="background-color: #e0f7ff;">
        <!-- Left text section -->
        <div class="footer-info text-muted">
            <span>${totalItems} Produk</span> |
            <span>Estimasi Harga: Rp ${estimatedPrice.toLocaleString()}</span>
        </div>
        
        <!-- Button on the right -->
        <button class="btn btn-primary d-flex align-items-center">
            <i class="tf-icons bx bx-cart me-2"></i> Keranjang Saya
        </button>
    </div>
    `;
  } else if (totalItems == 0) {
    htmlData = `
  <div class="container-xxl d-flex flex-wrap justify-content-between py-2 flex-md-row flex-column"  >
    <div class="mb-2 mb-md-0 text-center">
    © ${watermark}. HaloDog.
    </div>
  </div>
    `;
  }
  addStickyFooterStyles();
  $("footer").html(htmlData);
}

function pageButtons() {
  if (currentPage > totalPages) {
    if (totalPages > 0) {
      moveToPage(totalPages);
      return;
    } else {
      currentPage = 1;
    }
  }
  if (currentPage > 1) $("#prevBtn").removeClass("disabled");
  if (currentPage < totalPages) $("#nextBtn").removeClass("disabled");
}

function moveToPage(pageNumber) {
  currentPage = pageNumber;
  loadMedicalItems();
}

function nextPage() {
  if (currentPage + 1 <= totalPages) {
    currentPage++;
  }
  loadMedicalItems();
}

function previousPage() {
  if (currentPage - 1 > 0) {
    currentPage--;
  }
  loadMedicalItems();
}

function addStickyFooterStyles() {
  const existingStyle = document.getElementById("stickyFooterStyle");
  if (existingStyle) {
    existingStyle.parentNode.removeChild(existingStyle);
  }
  const style = document.createElement("style");
  style.setAttribute("id", "stickyFooterStyle");
  if (cartItems.length != 0) {
    style.innerHTML = `
    body, html {
      height: 100%;
      margin: 0;
      display: flex;
      flex-direction: column;
    }

    #content {
      flex: 1;
    }

    footer {
      background-color: #e0f7ff;
      position: sticky;
      bottom: 0;
      width: 100%;
      z-index: 1000; /* Ensure it stays above other elements if necessary */
    }
  `;
  }
  document.head.appendChild(style);
}
