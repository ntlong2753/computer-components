const casePcManager = new ComponentFormManager({
    prefix: 'casepc',
    apiEndpoint: '/admin/casepc/api',
    dropdownKeys: ['brand', 'model', 'formFactor', 'motherboardSupport', 'color'],
    placeholders: {
        brand: "-- Chọn Hãng --",
        model: "-- Chọn Model --",
        formFactor: "-- Chọn Kích thước --",
        motherboardSupport: "-- Chọn Hỗ trợ Mainboard --",
        color: "-- Chọn Màu sắc --"
    },
    generateNameCallback: function() {
        const brand = document.getElementById("casepcBrand").value || "";
        const model = document.getElementById("casepcModel").value || "";
        const color = document.getElementById("casepcColor").value || "";
        const formFactor = document.getElementById("casepcFormFactor").value || "";

        const parts = [brand, model];
        if (color && color.trim() !== "") {
            parts.push(color.trim());
        }
        if (formFactor && formFactor.trim() !== "") {
            parts.push(formFactor.trim());
        }

        const fullName = parts.map(p => p.trim()).filter(p => p !== "").join(" ");
        const nameInput = document.getElementById("casepcName");
        if (nameInput) nameInput.value = fullName;
    },
    populateEditForm: function(item) {
        // No specific non-dropdown fields to populate manually in populateEditForm
        // other than what's automatically mapped by id and custom dropdowns
    },
    renderRowHTML: function(casePc) {
        const descDisplay = (casePc.description && casePc.description.trim() !== "") ? casePc.description : "không có mô tả";
        
        return `
            <td>${casePc.id}</td>
            <td>${casePc.name}</td>
            <td>${casePc.brand || ''}</td>
            <td>${casePc.model || ''}</td>
            <td>${casePc.formFactor || ''}</td>
            <td>${casePc.motherboardSupport || ''}</td>
            <td>${casePc.color || ''}</td>
            <td>${casePc.price}</td>
            <td>${casePc.stock}</td>
            <td>${descDisplay}</td>
            <td>
                <div class="d-flex gap-2">
                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="casePcManager.editItemModal(${casePc.id})">Sửa</button>
                    <form action="/admin/casepc/${casePc.id}/delete" method="post" class="m-0">
                        <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Xóa Case này?')">Xóa</button>
                    </form>
                </div>
            </td>
        `;
    }
});

// Sync from existing server data if available
if (typeof existingCasePcs !== 'undefined' && Array.isArray(existingCasePcs)) {
    let changed = false;
    existingCasePcs.forEach(casePc => {
        ['brand', 'model', 'formFactor', 'motherboardSupport', 'color'].forEach(key => {
            const val = casePc[key];
            if (val && val.trim() !== "" && !casePcManager.customDropdownData[key].includes(val)) {
                casePcManager.customDropdownData[key].push(val);
                changed = true;
            }
        });
    });
    if (changed) {
        casePcManager.saveCustomDropdownData();
        casePcManager.initCustomDropdowns();
    }
}
