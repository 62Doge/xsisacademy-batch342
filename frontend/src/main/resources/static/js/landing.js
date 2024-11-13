let currentUserRoleId = USER_LOGGED_ID;

document.addEventListener("DOMContentLoaded", (event) => {
  loadMenuCards("no-role")
    .then(() => {
      if (IS_USER_LOGGED) {
        loadMenuCards(currentUserRoleId);
      }
    })
    .then(() => {
      const cariObat = $("#cariObat");
      if (cariObat.length) {
        cariObat.click(function (e) {
          e.preventDefault();
          loadMedicalItemModal();
          $("#baseModal").modal("show");
        });
      }
    });
});

function loadMenuCards(roleId) {
  return new Promise((resolve) => {
    $.ajax({
      url: `http://localhost:9001/api/admin/menu/role/${roleId}`,
      method: "GET",
      success: function (response) {
        if (response.status === 200) {
          const menus = response.data;
          const menuContainer = $("#menuContainer");
          menus.forEach((menu) => {
            const menuUrl = menu.url !== null ? menu.url : "#";
            if (menu.parentId === null) {
            }
            if (!menu.url && !menu.parentId) return;
            const capitalizedMenuName = menu.name
              .split(" ")
              .map((word) => {
                return word[0].toUpperCase() + word.substring(1);
              })
              .join(" ")
              .replace(/\s/g, "");
            const menuElementId =
              capitalizedMenuName.charAt(0).toLowerCase() +
              capitalizedMenuName.slice(1);
            const card = `
              <div class="col">
                <div class="card h-100 text-center">
                  <div class="card-body">
                    <h5 class="card-title">${menu.name}</h5>
                    <i class="${menu.bigIcon}" style="font-size: 5rem;"></i>
                    <a href="${menuUrl}" class="stretched-link" id="${menuElementId}"></a>
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
    setTimeout(() => {
      console.log(`Menu cards loaded for role: ${roleId}`);
      resolve(); // Resolve once done
    }, 200); // Simulate delay
  });
}

function addMenuToSidebar(menus) {
  let menuData = "";

  const Parent = [];
  const Child = [];

  $.each(menus, function (index, item) {
    if (item.childIds && item.childIds.length > 0) {
      Parent.push(item);
    } else {
      Child.push(item);
    }
  });

  $.each(Parent, function (index, menu) {
    const menuUrl = menu.url !== null ? menu.url : "#";
    menuData = `
      <li class="menu-item">
         <a href="javascript:void(0);" class="menu-link menu-toggle">
           <div class="col-2">
             <i class="${menu.smallIcon}"></i>
           </div>
           <div data-i18n="${menu.name}">${menu.name}</div>
         </a>
         <ul class="menu-sub" id="sub${menu.id}">
         
         </ul>
      </li>
      `;
    if (menu.parentId) {
      $(`#sub${menu.parentId}`).append(menuData);
    } else {
      $(".menu-inner").append(menuData);
    }
  });

  $.each(Child, function (index, menu) {
    const menuUrl = menu.url !== null ? menu.url : "#";
    menuData = `
     <li class="menu-item">
        <a href="${menuUrl}" class="menu-link">
          <div class="col-2">
            <i class="${menu.smallIcon}"></i>
          </div>
          <div data-i18n="${menu.name}">${menu.name}</div>
        </a>
     </li>
     `;
    if ($(`#sub${menu.parentId}`).length) {
      $(`#sub${menu.parentId}`).append(menuData);
    } else {
      $(".menu-inner").append(menuData);
    }
  });
}
