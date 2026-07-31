const ramManager = new ComponentFormManager({
    prefix: 'ram',
    apiEndpoint: '/admin/ram/api',
    dropdownKeys: ['brand', 'ramType'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        ramType: "-- Chọn Chuẩn RAM --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById('ramBrand').value || "";
        const capacity = document.getElementById('ramCapacity').value || "";
        const ramType = document.getElementById('ramRamType').value || "";
        const busSpeed = document.getElementById('ramBusSpeed').value || "";

        const nameField = document.getElementById('ramName');

        let parts = [];
        if (brand.trim() !== "") parts.push(brand.trim());
        if (capacity.trim() !== "") parts.push(capacity.trim() + "GB");
        if (ramType.trim() !== "") parts.push(ramType.trim());
        if (busSpeed.trim() !== "") parts.push(busSpeed.trim() + "MHz");

        if (parts.length > 0) {
            nameField.value = parts.join(" ");
        } else {
            nameField.value = "";
        }
    },
    populateEditForm: function(item) {
        const capacityInput = document.getElementById("ramCapacity");
        if (capacityInput) capacityInput.value = item.capacity || "";
        
        const busSpeedInput = document.getElementById("ramBusSpeed");
        if (busSpeedInput) busSpeedInput.value = item.busSpeed || "";
    },
    renderRowHTML: function(ram) {
        const descDisplay = (ram.description && ram.description.trim() !== "") ? ram.description : "không có mô tả";
        
        return `
            <td>${ram.id}</td>
            <td>${ram.name}</td>
            <td>${ram.brand || ''}</td>
            <td>${ram.capacity || ''}</td>
            <td>${ram.ramType || ''}</td>
            <td>${ram.busSpeed || ''}</td>
            <td>${ram.price}</td>
            <td>${ram.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="ramManager.editItemModal(${ram.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/ram/${ram.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa RAM này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingRams !== 'undefined' && Array.isArray(existingRams)) {
    let changed = false;
    existingRams.forEach(ram => {
        ['brand', 'ramType'].forEach(key => {
            const val = ram[key];
            if (val && val.trim() !== "" && !ramManager.customDropdownData[key].includes(val)) {
                ramManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });

    if (changed) {
        ramManager.saveCustomDropdownData();
        ramManager.initCustomDropdowns();
    }
}
