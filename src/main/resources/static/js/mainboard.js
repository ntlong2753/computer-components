const mainboardManager = new ComponentFormManager({
    prefix: 'mainboard',
    apiEndpoint: '/admin/mainboard/api',
    dropdownKeys: ['brand', 'model', 'socket', 'chipset', 'ramType', 'formFactor'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        model: "-- Chọn Model --",
        socket: "-- Chọn Socket --",
        chipset: "-- Chọn Chipset --",
        ramType: "-- Chọn Chuẩn RAM --",
        formFactor: "-- Chọn Kích thước --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById('mainboardBrand').value || "";
        const model = document.getElementById('mainboardModel').value || "";
        
        const nameField = document.getElementById('mainboardName');

        let parts = [];
        if (brand.trim() !== "") parts.push(brand.trim());
        if (model.trim() !== "") parts.push(model.trim());

        if (parts.length > 0) {
            nameField.value = "Mainboard " + parts.join(" ");
        } else {
            nameField.value = "";
        }
    },
    populateEditForm: function(item) {
        // Dropdowns are already handled by ComponentFormManager for single fields matching dropdownKeys.
        // We only need this if we have extra fields not handled by base class
    },
    renderRowHTML: function(item) {
        const descDisplay = (item.description && item.description.trim() !== "") ? item.description : "không có mô tả";

        return `
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.brand || ''}</td>
            <td>${item.model || ''}</td>
            <td>${item.socket || ''}</td>
            <td>${item.chipset || ''}</td>
            <td>${item.ramType || ''}</td>
            <td>${item.formFactor || ''}</td>
            <td>${item.price}</td>
            <td>${item.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="mainboardManager.editItemModal(${item.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/mainboard/${item.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa Mainboard này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingMainboards !== 'undefined' && Array.isArray(existingMainboards)) {
    let changed = false;
    existingMainboards.forEach(item => {
        ['brand', 'model', 'socket', 'chipset', 'ramType', 'formFactor'].forEach(key => {
            const val = item[key];
            if (val && val.trim() !== "" && !mainboardManager.customDropdownData[key].includes(val)) {
                mainboardManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });

    if (changed) {
        mainboardManager.saveCustomDropdownData();
        mainboardManager.initCustomDropdowns();
    }
}
