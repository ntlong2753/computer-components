const storageManager = new ComponentFormManager({
    prefix: 'storage',
    apiEndpoint: '/admin/storage/api',
    dropdownKeys: ['brand', 'connectionType', 'pcieGen'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        connectionType: "-- Chọn Chuẩn kết nối --",
        pcieGen: "-- Chọn Chuẩn PCIe --"
    },
    generateNameCallback: function() {
        const type = document.getElementById('storageType').value;
        const brand = document.getElementById('storageBrand').value || "";
        const capacity = document.getElementById('storageCapacity').value || "";
        const capacityUnit = document.getElementById('storageCapacityUnit').value || "";
        
        const nameField = document.getElementById('storageName');

        let parts = [];
        if (brand.trim() !== "") parts.push(brand.trim());
        
        // Push "HDD" or "SSD"
        if (type === "HDD" || type === "SSD") {
            parts.push(type);
        }

        if (capacity.trim() !== "" && capacityUnit.trim() !== "") {
            parts.push(capacity.trim() + capacityUnit.trim());
        }

        if (type === "SSD") {
            const connectionType = document.getElementById('storageConnectionType').value || "";
            if (connectionType.trim() !== "") parts.push(connectionType.trim());
        }

        if (parts.length > 0) {
            nameField.value = "Ổ cứng " + parts.join(" ");
        } else {
            nameField.value = "";
        }
    },
    populateEditForm: function(item) {
        // Set type dropdown
        const typeInput = document.getElementById("storageType");
        if (typeInput) {
            typeInput.value = item.type || "SSD";
        }
        
        // Trigger toggle logic
        toggleStorageFields();

        // Common
        const capacityInput = document.getElementById("storageCapacity");
        if (capacityInput) capacityInput.value = item.capacity || "";
        
        const capacityUnitInput = document.getElementById("storageCapacityUnit");
        if (capacityUnitInput) capacityUnitInput.value = item.capacityUnit || "GB";
        
        const cacheInput = document.getElementById("storageCache");
        if (cacheInput) cacheInput.value = item.cache || "";

        const cacheUnitInput = document.getElementById("storageCacheUnit");
        if (cacheUnitInput) cacheUnitInput.value = item.cacheUnit || "MB";

        // HDD
        const rpmInput = document.getElementById("storageRpm");
        if (rpmInput) rpmInput.value = item.rpm || "";

        // SSD
        const readSpeedInput = document.getElementById("storageReadSpeed");
        if (readSpeedInput) readSpeedInput.value = item.readSpeed || "";
        
        const writeSpeedInput = document.getElementById("storageWriteSpeed");
        if (writeSpeedInput) writeSpeedInput.value = item.writeSpeed || "";
    },
    renderRowHTML: function(item) {
        const descDisplay = (item.description && item.description.trim() !== "") ? item.description : "không có mô tả";
        
        // Format specs based on type
        let specStr = "";
        if (item.type === 'HDD') {
            specStr = `RPM: ${item.rpm || '?'} | Cache: ${item.cache || '?'}${item.cacheUnit || ''}`;
        } else {
            specStr = `Read: ${item.readSpeed || '?'}MB/s | Write: ${item.writeSpeed || '?'}MB/s | Cache: ${item.cache || '?'}${item.cacheUnit || ''}`;
        }

        return `
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.brand || ''}</td>
            <td><span class="badge ${item.type === 'HDD' ? 'badge-primary' : 'badge-success'}">${item.type || ''}</span></td>
            <td>${item.capacity || ''} ${item.capacityUnit || ''}</td>
            <td>
                ${item.type === 'SSD' ? ((item.connectionType || '') + ' ' + (item.pcieGen || '')) : '-'}
            </td>
            <td><small>${specStr}</small></td>
            <td>${item.price}</td>
            <td>${item.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="storageManager.editItemModal(${item.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/storage/${item.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa Ổ cứng này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

function toggleStorageFields() {
    const type = document.getElementById('storageType').value;
    const hddFields = document.getElementById('hdd-fields');
    const ssdFields = document.getElementById('ssd-fields');
    
    if (type === 'HDD') {
        if (hddFields) hddFields.style.display = 'block';
        if (ssdFields) ssdFields.style.display = 'none';
        
        // Reset cache unit default for HDD if empty
        const cacheUnit = document.getElementById("storageCacheUnit");
        if (cacheUnit && !cacheUnit.value) {
            cacheUnit.value = "MB";
        }
    } else {
        if (hddFields) hddFields.style.display = 'none';
        if (ssdFields) ssdFields.style.display = 'block';
        
        // Reset cache unit default for SSD if empty
        const cacheUnit = document.getElementById("storageCacheUnit");
        if (cacheUnit && !cacheUnit.value) {
            cacheUnit.value = "GB";
        }
    }
    
    if (!storageManager.isNameManuallyEdited) {
        storageManager.generateNameCallback();
    }
}

// Bind event for Type dropdown
document.addEventListener('DOMContentLoaded', () => {
    const typeDropdown = document.getElementById('storageType');
    if (typeDropdown) {
        typeDropdown.addEventListener('change', toggleStorageFields);
        toggleStorageFields(); // init
    }
});

// Sync from existing server data if available
if (typeof existingStorages !== 'undefined' && Array.isArray(existingStorages)) {
    let changed = false;
    existingStorages.forEach(item => {
        ['brand', 'connectionType', 'pcieGen'].forEach(key => {
            const val = item[key];
            if (val && val.trim() !== "" && !storageManager.customDropdownData[key].includes(val)) {
                storageManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });

    if (changed) {
        storageManager.saveCustomDropdownData();
        storageManager.initCustomDropdowns();
    }
}
