    // lấy cái popup (modal) thêm/sửa cpu
    const modal = document.getElementById("cpuModal");

    // → 3 nút: nút mở modal, nút đóng (x), nút hủy
    const openBtn = document.getElementById("openCreateModalBtn");
    const closeBtn = document.getElementById("closeModalBtn");
    const cancelBtn = document.getElementById("cancelModalBtn");

    // form nhập thông tin cpu.
    const form = document.getElementById("cpuAjaxForm");

    // ô thông báo lưu thành công.
    const successAlert = document.getElementById("successAlert");

    const tbody = document.querySelector("#cpuTable tbody");
    const emptyRow = document.getElementById("emptyRow");

    // ô chọn file ảnh, và khung để hiển thị ảnh xem trước
    const cpuImagesInput = document.getElementById("cpuImagesInput");
    const previewContainer = document.getElementById("imagePreviewContainer");


    let selectedFiles = [];
    let existingImages = [];
    let deletedImageIds = [];
    let isNameManuallyEdited = false;

    // Dữ liệu cho Custom Dropdown - BAN ĐẦU ĐỂ TRỐNG (KHÔNG CODE CỨNG), HOẶC TẢI TỪ LOCALSTORAGE
    let customDropdownData = JSON.parse(localStorage.getItem('cpuCustomDropdownData')) || {
        brand: [],
        series: [],
        segment: [],
        suffix: [],
        socket: []
    };

    function saveCustomDropdownData() {
        localStorage.setItem('cpuCustomDropdownData', JSON.stringify(customDropdownData));
    }

    const placeholders = {
        brand: "-- Chọn Hãng --",
        series: "-- Chọn Dòng sản phẩm --",
        segment: "-- Chọn Phân khúc --",
        suffix: "-- Chọn Hậu tố --",
        socket: "-- Chọn Socket --"
    };

    function capitalize(s) {
        return s.charAt(0).toUpperCase() + s.slice(1);
    }

    function toggleDropdown(key) {
        const container = document.getElementById("select-container-" + key);
        const isOpen = container.classList.contains("open");
        closeAllDropdowns();
        if (!isOpen) {
            container.classList.add("open");
        }
    }

    function closeAllDropdowns() {
        document.querySelectorAll(".custom-select-container").forEach(c => c.classList.remove("open"));
    }

    document.addEventListener("click", function(e) {
        if (!e.target.closest(".custom-select-container")) {
            closeAllDropdowns();
        }
    });

    function renderCustomDropdown(key) {
        const listEl = document.getElementById("options-list-" + key);
        const hiddenInput = document.getElementById("cpu" + capitalize(key));
        const triggerText = document.getElementById("trigger-text-" + key);
        const currentVal = hiddenInput.value;

        listEl.innerHTML = "";

        if (customDropdownData[key].length === 0) {
            const emptyLi = document.createElement("li");
            emptyLi.className = "empty-hint";
            emptyLi.innerText = "Chưa có tùy chọn nào. Thêm mới ở dưới...";
            listEl.appendChild(emptyLi);
        } else {
            customDropdownData[key].forEach((val, idx) => {
                const li = document.createElement("li");
                const isSelected = val === currentVal;
                li.className = "custom-option-item" + (isSelected ? " selected" : "");

                const textSpan = document.createElement("span");
                textSpan.className = "option-text";
                textSpan.innerText = val;
                textSpan.onclick = function() {
                    selectCustomOption(key, val);
                };

                const actionsDiv = document.createElement("div");
                actionsDiv.className = "option-actions";

                const editBtn = document.createElement("button");
                editBtn.type = "button";
                editBtn.className = "action-icon-btn btn-edit";
                editBtn.innerHTML = "✏️";
                editBtn.title = "Sửa tùy chọn này";
                editBtn.onclick = function(e) {
                    e.stopPropagation();
                    editCustomOption(key, idx);
                };

                const deleteBtn = document.createElement("button");
                deleteBtn.type = "button";
                deleteBtn.className = "action-icon-btn btn-delete";
                deleteBtn.innerHTML = "❌";
                deleteBtn.title = "Xóa tùy chọn này";
                deleteBtn.onclick = function(e) {
                    e.stopPropagation();
                    deleteCustomOption(key, idx);
                };

                actionsDiv.appendChild(editBtn);
                actionsDiv.appendChild(deleteBtn);

                li.appendChild(textSpan);
                li.appendChild(actionsDiv);
                listEl.appendChild(li);
            });
        }

        triggerText.innerText = currentVal ? currentVal : placeholders[key];
    }

    function selectCustomOption(key, val) {
        document.getElementById("cpu" + capitalize(key)).value = val;
        renderCustomDropdown(key);
        closeAllDropdowns();
        autoGenerateCpuName();
    }

    function addCustomOption(key) {
        const input = document.getElementById("add-input-" + key);
        const newVal = input.value.trim();
        if (newVal !== "") {
            if (!customDropdownData[key].includes(newVal)) {
                customDropdownData[key].push(newVal);
                saveCustomDropdownData();
            }
            input.value = "";
            selectCustomOption(key, newVal);
        }
    }

    function editCustomOption(key, idx) {
        const oldVal = customDropdownData[key][idx];
        const newVal = prompt("Sửa tên tùy chọn:", oldVal);
        if (newVal && newVal.trim() !== "" && newVal.trim() !== oldVal) {
            const trimmed = newVal.trim();
            customDropdownData[key][idx] = trimmed;
            const hiddenInput = document.getElementById("cpu" + capitalize(key));
            if (hiddenInput.value === oldVal) {
                hiddenInput.value = trimmed;
            }
            renderCustomDropdown(key);
            autoGenerateCpuName();
            saveCustomDropdownData();
        }
    }

    function deleteCustomOption(key, idx) {
        const valToRemove = customDropdownData[key][idx];
        if (confirm(`Bạn có chắc muốn xóa tùy chọn "${valToRemove}"?`)) {
            customDropdownData[key].splice(idx, 1);
            const hiddenInput = document.getElementById("cpu" + capitalize(key));
            if (hiddenInput.value === valToRemove) {
                hiddenInput.value = "";
            }
            renderCustomDropdown(key);
            autoGenerateCpuName();
            saveCustomDropdownData();
        }
    }

    // Tự động sinh tên CPU khi thay đổi các trường cấu thành
    document.getElementById("cpuName").addEventListener("input", function() {
        isNameManuallyEdited = true;
    });

    function autoGenerateCpuName() {
        if (isNameManuallyEdited) return;

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

        document.getElementById("cpuName").value = fullName;
    }

    // Sự kiện chọn file ảnh
    cpuImagesInput.addEventListener("change", function(e) {
        const files = Array.from(e.target.files);
        files.forEach(file => {
            if (!selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
                selectedFiles.push(file);
            }
        });
        renderPreviews();
        cpuImagesInput.value = "";
    });

    function renderPreviews() {
        previewContainer.innerHTML = "";
        
        // Render existing images from server
        existingImages.forEach((imgObj, index) => {
            const item = document.createElement("div");
            item.className = "image-preview-item";

            const img = document.createElement("img");
            let src = imgObj.imageUrl;
            if (!src.startsWith("http")) {
                src = src.startsWith("/") ? src : '/' + src;
            }
            img.src = src; 

            const removeBtn = document.createElement("button");
            removeBtn.type = "button";
            removeBtn.className = "remove-btn";
            removeBtn.innerHTML = "&times;";
            removeBtn.title = "Xóa ảnh cũ này";
            removeBtn.onclick = function() {
                deletedImageIds.push(imgObj.id);
                existingImages.splice(index, 1);
                renderPreviews();
            };

            item.appendChild(img);
            item.appendChild(removeBtn);
            previewContainer.appendChild(item);
        });

        // Render new selected files
        selectedFiles.forEach((file, index) => {
            const item = document.createElement("div");
            item.className = "image-preview-item";

            const img = document.createElement("img");
            img.src = URL.createObjectURL(file);

            const removeBtn = document.createElement("button");
            removeBtn.type = "button";
            removeBtn.className = "remove-btn";
            removeBtn.innerHTML = "&times;";
            removeBtn.title = "Bỏ ảnh mới này";
            removeBtn.onclick = function() {
                selectedFiles.splice(index, 1);
                renderPreviews();
            };

            item.appendChild(img);
            item.appendChild(removeBtn);
            previewContainer.appendChild(item);
        });
    }

    function initCustomDropdowns() {
        ['brand', 'series', 'segment', 'suffix', 'socket'].forEach(key => {
            renderCustomDropdown(key);
        });
    }

    function openModal() {
        form.reset();
        selectedFiles = [];
        existingImages = [];
        deletedImageIds = [];
        isNameManuallyEdited = false;
        renderPreviews();
        
        // Reset hidden values to empty
        document.getElementById("cpuBrand").value = "";
        document.getElementById("cpuSeries").value = "";
        document.getElementById("cpuSegment").value = "";
        document.getElementById("cpuSuffix").value = "";
        document.getElementById("cpuSocket").value = "";

        initCustomDropdowns();

        document.getElementById("cpuId").value = "";
        document.getElementById("cpuName").value = "";
        document.getElementById("modalTitle").innerText = "Thêm CPU mới";
        successAlert.style.display = "none";
        clearErrors();
        
        autoGenerateCpuName();
        modal.style.display = "block";
    }

    function closeModal() {
        modal.style.display = "none";
        closeAllDropdowns();
    }

    function clearErrors() {
        document.querySelectorAll('.error-msg').forEach(el => el.innerText = "");
    }

    openBtn.onclick = openModal;
    closeBtn.onclick = closeModal;
    cancelBtn.onclick = closeModal;

    window.onclick = function(event) {
        if (event.target == modal) {
            closeModal();
        }
    }

    form.onsubmit = function(e) {
        e.preventDefault();
        clearErrors();
        successAlert.style.display = "none";

        const descInput = document.getElementById("cpuDesc");
        if (!descInput.value || descInput.value.trim() === "") {
            descInput.value = "không có mô tả";
        }

        const formData = new FormData(form);

        selectedFiles.forEach(file => {
            formData.append("files", file);
        });

        deletedImageIds.forEach(id => {
            formData.append("deletedImageIds", id);
        });

        fetch('/admin/cpu/api/save', {
            method: 'POST',
            body: formData
        })
        .then(async response => {
            if (response.ok) {
                const data = await response.json();
                handleSuccess(data);
            } else {
                const errorData = await response.json();
                handleValidationErrors(errorData);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("err-general").innerText = "Đã xảy ra lỗi hệ thống!";
        });
    };

    function handleSuccess(cpu) {
        successAlert.style.display = "block";
        successAlert.innerText = "Lưu thành công CPU: " + cpu.name;
        document.getElementById("modalTitle").innerText = "Sửa CPU (đã lưu)";
        document.getElementById("cpuId").value = cpu.id; 
        
        selectedFiles = [];
        deletedImageIds = [];
        renderPreviews();

        if (emptyRow) emptyRow.style.display = 'none';

        let existingRow = document.getElementById('cpu-row-' + cpu.id);
        const descDisplay = (cpu.description && cpu.description.trim() !== "") ? cpu.description : "không có mô tả";
        const suffixDisplay = (cpu.suffix && cpu.suffix.trim() !== "") ? cpu.suffix : "không có";

        const rowHTML = `
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
                <button type="button" style="cursor:pointer;" onclick="editCpuModal(${cpu.id})">Sửa</button>
                &nbsp;|&nbsp;
                <form action="/admin/cpu/${cpu.id}/delete" method="post" style="display:inline">
                    <button type="submit" onclick="return confirm('Xóa CPU này?')">Xóa</button>
                </form>
            </td>
        `;

        if (existingRow) {
            existingRow.innerHTML = rowHTML;
        } else {
            const tr = document.createElement("tr");
            tr.id = 'cpu-row-' + cpu.id;
            tr.innerHTML = rowHTML;
            tbody.appendChild(tr);
        }

        updateTableSTT();
    }

    function updateTableSTT() {
        const rows = document.querySelectorAll("#cpuTable tbody tr:not(#emptyRow)");
        rows.forEach((row, idx) => {
            const firstTd = row.querySelector("td");
            if (firstTd) {
                firstTd.innerText = idx + 1;
            }
        });
    }

    function editCpuModal(id) {
        fetch('/admin/cpu/api/' + id)
            .then(res => {
                if (!res.ok) throw new Error("Không tìm thấy CPU");
                return res.json();
            })
            .then(data => {
                const cpu = data.cpu;
                existingImages = data.images || [];
                deletedImageIds = [];

                form.reset();
                selectedFiles = [];
                renderPreviews();
                clearErrors();
                successAlert.style.display = "none";

                document.getElementById("cpuTitle") ? document.getElementById("cpuTitle") : null;
                document.getElementById("modalTitle").innerText = "Sửa CPU #" + cpu.id;
                document.getElementById("cpuId").value = cpu.id;
                document.getElementById("cpuName").value = cpu.name || "";
                document.getElementById("cpuModelNumber").value = cpu.modelNumber || "";
                document.getElementById("cpuPrice").value = cpu.price || "";
                document.getElementById("cpuStock").value = cpu.stock || "";
                document.getElementById("cpuImageUrl").value = cpu.imageUrl || "";
                document.getElementById("cpuDesc").value = cpu.description || "";

                ['brand', 'series', 'segment', 'suffix', 'socket'].forEach(key => {
                    const val = cpu[key] || "";
                    if (val && !customDropdownData[key].includes(val)) {
                        customDropdownData[key].push(val);
                        saveCustomDropdownData();
                    }
                    selectCustomOption(key, val);
                });

                isNameManuallyEdited = true;
                renderPreviews();
                modal.style.display = "block";
            })
            .catch(err => {
                alert(err.message);
            });
    }

    function handleValidationErrors(errors) {
        for (const [key, value] of Object.entries(errors)) {
            const errorElement = document.getElementById("err-" + key);
            if (errorElement) {
                errorElement.innerText = value;
            } else if (key === 'general') {
                document.getElementById("err-general").innerText = value;
            }
        }
    }

    // Initialize custom dropdowns & STT on load
    initCustomDropdowns();
    updateTableSTT();
