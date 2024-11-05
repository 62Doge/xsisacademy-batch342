let currentUserRoleId = 1;

document.addEventListener("DOMContentLoaded", (event) => {
  // $("#layout-menu").addClass("d-none");
});

$(document).ready(function () {
  loadMenuCards(currentUserRoleId);
});

function loadMenuCards(roleId) {
  $.ajax({
    url: `http://localhost:9001/api/admin/menu/role/${roleId}`,
    method: "GET",
    success: function (response) {
      if (response.status === 200) {
        const menus = response.data;
        console.log(menus);
        console.log(response);
        const menuContainer = $("#menuContainer");
        menus.forEach((menu) => {
          const card = `
              <div class="col">
                <div class="card h-100 text-center">
                  <div class="card-body">
                    <h5 class="card-title">${menu.name}</h5>
                    ${
                      menu.bigIcon
                        ? `<img src="${menu.bigIcon}" class="card-icon" alt="Icon">`
                        : ""
                    }
                    <p class="card-text">Description for ${menu.name}</p>
                    <a href="${menu.url}" class="stretched-link"></a>
                  </div>
                </div>
              </div>
            `;
          menuContainer.append(card);
        });
      }
    },
    error: function (error) {
      console.error("Error fetching menu data:", error);
    },
  });
}
