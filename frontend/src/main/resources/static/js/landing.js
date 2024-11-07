let currentUserRoleId = 2;

$(document).ready(function () {
  loadMenuCards("no-role");
  if (currentUserRoleId !== -1 && currentUserRoleId !== null) {
    IS_USER_LOGGED = true;
    loadMenuCards(currentUserRoleId);
  } else {
    IS_USER_LOGGED = false;
  }
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
                        : `<img src="${menu.smallIcon}" class="card-icon" alt="Icon">`
                    }
                    <a href="${menu.url}" class="stretched-link"></a>
                  </div>
                </div>
              </div>
            `;
          menuContainer.append(card);
        });
        if (roleId !== "no-role") addMenuToSidebar(menus);
      }
    },
    error: function (error) {
      console.error("Error fetching menu data:", error);
    },
  });
}

function addMenuToSidebar(menus) {
  let menuData = "";
  $.each(menus, function (index, menu) {
    menuData += `
     <li class="menu-item">
        <a href="${menu.url}" class="menu-link">
          ${
          menu.smallIcon
            ? `<img src="${menu.smallIcon}" alt="${menu.name} icon" style="width: 1.5em; height: 1.5em; vertical-align: middle; fill: currentColor; margin-right: 0.5em;">`
            : `<img src="${menu.BigIcon}" alt="${menu.name} icon" style="width: 1.5em; height: 1.5em; vertical-align: middle; fill: currentColor; margin-right: 0.5em;">`
          }
          <div data-i18n="${menu.name}">${menu.name}</div>
        </a>
     </li>
     `;
  });
  $(".menu-inner").append(menuData);
}
