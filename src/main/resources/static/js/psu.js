const psuManager = new ComponentFormManager({
    prefix: 'psu',
    apiEndpoint: '/admin/psu/api',
    dropdownKeys: ['brand', 'wattage', 'efficiency', 'formFactor'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        wattage: "-- Chọn Công suất --",
        efficiency: "-- Chọn Chuẩn nguồn --",
        formFactor: "-- Chọn Kích thước --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById('psuBrand').value || "";
        const wattage = document.getElementById('psuWattage').value || "";
        const efficiency = document.getElementById('psuEfficiency').value || "";
        
        const nameField = document.getElementById('psuName');

        let parts = [];
        if (brand.trim() !== "") parts.push(brand.trim());
        if (wattage.trim() !== "") parts.push(wattage.trim() + "W");
        if (efficiency.trim() !== "") parts.push(efficiency.trim());

        if (parts.length > 0) {
            nameField.value = "Nguồn " + parts.join(" ");
        } else {
            nameField.value = "";
        }
    },
    populateEditForm: function(item) {
        // Dropdowns are handled automatically, including wattage now!
    },
    renderRowHTML: function(item) {
        const descDisplay = (item.description && item.description.trim() !== "") ? item.description : "không có mô tả";

        return `
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.brand || ''}</td>
            <td>${item.wattage ? item.wattage + 'W' : ''}</td>
            <td>${item.efficiency || ''}</td>
            <td>${item.formFactor || ''}</td>
            <td>${item.price}</td>
            <td>${item.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="psuManager.editItemModal(${item.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/psu/${item.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa Nguồn này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingPsus !== 'undefined' && Array.isArray(existingPsus)) {
    let changed = false;
    existingPsus.forEach(item => {
        ['brand', 'wattage', 'efficiency', 'formFactor'].forEach(key => {
            const val = item[key];
            if (val !== undefined && val !== null && val.toString().trim() !== "" && !psuManager.customDropdownData[key].includes(val.toString().trim())) {
                psuManager.customDropdownData[key].push(val.toString().trim());
                changed = true;
            }
        });
    });

    if (changed) {
        psuManager.saveCustomDropdownData();
        psuManager.initCustomDropdowns();
    }
}
