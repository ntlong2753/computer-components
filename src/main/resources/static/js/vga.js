const vgaManager = new ComponentFormManager({
    prefix: 'vga',
    apiEndpoint: '/admin/vga/api',
    dropdownKeys: ['gpuBrand', 'gpuModel', 'brand', 'memoryType'],
    placeholders: {
        gpuBrand: "-- Chọn Hãng GPU --",
        gpuModel: "-- Chọn Model GPU --",
        brand: "-- Chọn Hãng card --",
        memoryType: "-- Chọn Loại VRAM --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById("vgaBrand").value || "";
        const gpuBrand = document.getElementById("vgaGpuBrand").value || "";
        const gpuModel = document.getElementById("vgaGpuModel").value || "";
        const vram = document.getElementById("vgaVram").value || "";
        const memoryType = document.getElementById("vgaMemoryType").value || "";

        const parts = [brand, gpuModel];
        if (vram && vram.trim() !== "") {
            parts.push(vram.trim() + "GB");
        }
        if (memoryType && memoryType.trim() !== "") {
            parts.push(memoryType.trim());
        }

        const fullName = parts.map(p => p.trim()).filter(p => p !== "").join(" ");
        const nameInput = document.getElementById("vgaName");
        if (nameInput) nameInput.value = fullName;
    },
    populateEditForm: function(item) {
        const vramInput = document.getElementById("vgaVram");
        if (vramInput) vramInput.value = item.vram || "";
    },
    renderRowHTML: function(vga) {
        const descDisplay = (vga.description && vga.description.trim() !== "") ? vga.description : "không có mô tả";
        const vramDisplay = (vga.vram) ? vga.vram + " GB" : "";
        
        return `
            <td>${vga.id}</td>
            <td>${vga.name}</td>
            <td>${vga.gpuBrand || ''}</td>
            <td>${vga.gpuModel || ''}</td>
            <td>${vga.brand || ''}</td>
            <td>${vramDisplay}</td>
            <td>${vga.memoryType || ''}</td>
            <td>${vga.price}</td>
            <td>${vga.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="vgaManager.editItemModal(${vga.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/vga/${vga.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa VGA này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingVgas !== 'undefined' && Array.isArray(existingVgas)) {
    let changed = false;
    existingVgas.forEach(vga => {
        ['gpuBrand', 'gpuModel', 'brand', 'memoryType'].forEach(key => {
            const val = vga[key];
            if (val && val.trim() !== "" && !vgaManager.customDropdownData[key].includes(val)) {
                vgaManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });
    if (changed) {
        vgaManager.saveCustomDropdownData();
        vgaManager.initCustomDropdowns();
    }
}
