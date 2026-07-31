const cpuManager = new ComponentFormManager({
    prefix: 'cpu',
    apiEndpoint: '/admin/cpu/api',
    dropdownKeys: ['brand', 'series', 'segment', 'suffix', 'socket'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        series: "-- Chọn Dòng sản phẩm --",
        segment: "-- Chọn Phân khúc --",
        suffix: "-- Chọn Hậu tố --",
        socket: "-- Chọn Socket --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById("cpuBrand").value || "";
        const series = document.getElementById("cpuSeries").value || "";
        const segment = document.getElementById("cpuSegment").value || "";
        const model = document.getElementById("cpuModelNumber").value || "";
        const suffix = document.getElementById("cpuSuffix").value || "";

        const parts = [brand, series, segment, model].map(p => p.trim()).filter(p => p !== "");
        let fullName = parts.join(" ");
        if (suffix && suffix.trim() !== "") {
            fullName = (fullName ? fullName + " " : "") + suffix.trim();
        }

        const nameInput = document.getElementById("cpuName");
        if (nameInput) nameInput.value = fullName;
    },
    populateEditForm: function(item) {
        const modelNumberInput = document.getElementById("cpuModelNumber");
        if (modelNumberInput) modelNumberInput.value = item.modelNumber || "";
        
        const coreCountInput = document.getElementById("cpuCoreCount");
        if (coreCountInput) coreCountInput.value = item.coreCount || "";
        
        const threadCountInput = document.getElementById("cpuThreadCount");
        if (threadCountInput) threadCountInput.value = item.threadCount || "";
    },
    renderRowHTML: function(cpu) {
        const descDisplay = (cpu.description && cpu.description.trim() !== "") ? cpu.description : "không có mô tả";
        const suffixDisplay = (cpu.suffix && cpu.suffix.trim() !== "") ? cpu.suffix : "không có";
        
        return `
            <td>${cpu.id}</td>
            <td>${cpu.name}</td>
            <td>${cpu.brand || ''}</td>
            <td>${cpu.series || ''}</td>
            <td>${cpu.segment || ''}</td>
            <td>${cpu.modelNumber || ''}</td>
            <td>${suffixDisplay}</td>
            <td>${cpu.price}</td>
            <td>${cpu.stock}</td>
            <td>${cpu.socket || ''}</td>
            <td>${descDisplay}</td>
            <td>
                <button type="button" style="cursor:pointer;" onclick="cpuManager.editItemModal(${cpu.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/cpu/${cpu.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa CPU này?')">Xóa</button>
                </form>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingCpus !== 'undefined' && Array.isArray(existingCpus)) {
    let changed = false;
    existingCpus.forEach(cpu => {
        ['brand', 'series', 'segment', 'suffix', 'socket'].forEach(key => {
            const val = cpu[key];
            if (val && val.trim() !== "" && !cpuManager.customDropdownData[key].includes(val)) {
                cpuManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });
    if (changed) {
        cpuManager.saveCustomDropdownData();
        cpuManager.initCustomDropdowns();
    }
}
