class ComponentFormManager {
    constructor(config) {
        this.prefix = config.prefix; // e.g., 'cpu' or 'vga'
        this.apiEndpoint = config.apiEndpoint; // e.g., '/admin/cpu/api'
        this.dropdownKeys = config.dropdownKeys || []; // ['brand', 'series', ...]
        this.placeholders = config.placeholders || {};
        this.generateNameCallback = config.generateNameCallback || function() {};
        this.renderRowHTML = config.renderRowHTML; // function(item) returns HTML string
        this.populateEditForm = config.populateEditForm; // function(item)

        // DOM Elements
        this.modal = document.getElementById(this.prefix + "Modal");
        this.form = document.getElementById(this.prefix + "AjaxForm");
        this.openBtn = document.getElementById("openCreateModalBtn");
        this.closeBtn = document.getElementById("closeModalBtn");
        this.cancelBtn = document.getElementById("cancelModalBtn");
        this.successAlert = document.getElementById("successAlert");
        this.tbody = document.querySelector("#" + this.prefix + "Table tbody");
        this.emptyRow = document.getElementById("emptyRow");
        this.imagesInput = document.getElementById(this.prefix + "ImagesInput");
        this.previewContainer = document.getElementById("imagePreviewContainer");
        this.nameInput = document.getElementById(this.prefix + "Name");

        // State
        this.selectedFiles = [];
        this.existingImages = [];
        this.deletedImageIds = [];
        this.isNameManuallyEdited = false;
        
        this.storageKey = this.prefix + 'CustomDropdownData';
        this.customDropdownData = JSON.parse(localStorage.getItem(this.storageKey)) || {};
        this.dropdownKeys.forEach(key => {
            if (!this.customDropdownData[key]) {
                this.customDropdownData[key] = [];
            }
        });

        this.init();
    }

    init() {
        this.initCustomDropdowns();
        this.updateTableSTT();
        this.bindEvents();
    }

    capitalize(s) {
        return s.charAt(0).toUpperCase() + s.slice(1);
    }

    saveCustomDropdownData() {
        localStorage.setItem(this.storageKey, JSON.stringify(this.customDropdownData));
    }

    closeAllDropdowns() {
        document.querySelectorAll(".custom-select-container").forEach(c => c.classList.remove("open"));
    }

    toggleDropdown(key) {
        const container = document.getElementById("select-container-" + key);
        if (container) {
            const isOpen = container.classList.contains("open");
            this.closeAllDropdowns();
            if (!isOpen) {
                container.classList.add("open");
            }
        }
    }

    renderCustomDropdown(key) {
        const listEl = document.getElementById("options-list-" + key);
        if (!listEl) return;
        
        const hiddenInputId = this.prefix + this.capitalize(key);
        const hiddenInput = document.getElementById(hiddenInputId);
        const triggerText = document.getElementById("trigger-text-" + key);
        const currentVal = hiddenInput ? hiddenInput.value : "";

        listEl.innerHTML = "";

        if (this.customDropdownData[key].length === 0) {
            const emptyLi = document.createElement("li");
            emptyLi.className = "empty-hint";
            emptyLi.innerText = "Chưa có tùy chọn nào. Thêm mới ở dưới...";
            listEl.appendChild(emptyLi);
        } else {
            this.customDropdownData[key].forEach((val, idx) => {
                const li = document.createElement("li");
                const isSelected = val === currentVal;
                li.className = "custom-option-item" + (isSelected ? " selected" : "");

                const textSpan = document.createElement("span");
                textSpan.className = "option-text";
                textSpan.innerText = val;
                textSpan.onclick = () => {
                    this.selectCustomOption(key, val);
                };

                const actionsDiv = document.createElement("div");
                actionsDiv.className = "option-actions";

                const editBtn = document.createElement("button");
                editBtn.type = "button";
                editBtn.className = "action-icon-btn btn-edit";
                editBtn.innerHTML = "✏️";
                editBtn.title = "Sửa tùy chọn này";
                editBtn.onclick = (e) => {
                    e.stopPropagation();
                    this.editCustomOption(key, idx);
                };

                const deleteBtn = document.createElement("button");
                deleteBtn.type = "button";
                deleteBtn.className = "action-icon-btn btn-delete";
                deleteBtn.innerHTML = "❌";
                deleteBtn.title = "Xóa tùy chọn này";
                deleteBtn.onclick = (e) => {
                    e.stopPropagation();
                    this.deleteCustomOption(key, idx);
                };

                actionsDiv.appendChild(editBtn);
                actionsDiv.appendChild(deleteBtn);

                li.appendChild(textSpan);
                li.appendChild(actionsDiv);
                listEl.appendChild(li);
            });
        }

        if (triggerText) {
            triggerText.innerText = currentVal ? currentVal : (this.placeholders[key] || "-- Chọn --");
        }
    }

    selectCustomOption(key, val) {
        const hiddenInputId = this.prefix + this.capitalize(key);
        const hiddenInput = document.getElementById(hiddenInputId);
        if (hiddenInput) {
            hiddenInput.value = val;
        }
        this.renderCustomDropdown(key);
        this.closeAllDropdowns();
        
        if (!this.isNameManuallyEdited) {
            this.generateNameCallback();
        }
    }

    addCustomOption(key, inputId) {
        const input = document.getElementById(inputId);
        if (!input) return;
        const newVal = input.value.trim();
        if (newVal !== "") {
            if (!this.customDropdownData[key].includes(newVal)) {
                this.customDropdownData[key].push(newVal);
                this.saveCustomDropdownData();
            }
            input.value = "";
            this.selectCustomOption(key, newVal);
        }
    }

    editCustomOption(key, idx) {
        const oldVal = this.customDropdownData[key][idx];
        const newVal = prompt("Sửa tên tùy chọn:", oldVal);
        if (newVal && newVal.trim() !== "" && newVal.trim() !== oldVal) {
            const trimmed = newVal.trim();
            this.customDropdownData[key][idx] = trimmed;
            const hiddenInput = document.getElementById(this.prefix + this.capitalize(key));
            if (hiddenInput && hiddenInput.value === oldVal) {
                hiddenInput.value = trimmed;
            }
            this.renderCustomDropdown(key);
            if (!this.isNameManuallyEdited) {
                this.generateNameCallback();
            }
            this.saveCustomDropdownData();
        }
    }

    deleteCustomOption(key, idx) {
        const valToRemove = this.customDropdownData[key][idx];
        if (confirm(`Bạn có chắc muốn xóa tùy chọn "${valToRemove}"?`)) {
            this.customDropdownData[key].splice(idx, 1);
            const hiddenInput = document.getElementById(this.prefix + this.capitalize(key));
            if (hiddenInput && hiddenInput.value === valToRemove) {
                hiddenInput.value = "";
            }
            this.renderCustomDropdown(key);
            if (!this.isNameManuallyEdited) {
                this.generateNameCallback();
            }
            this.saveCustomDropdownData();
        }
    }

    initCustomDropdowns() {
        this.dropdownKeys.forEach(key => {
            this.renderCustomDropdown(key);
        });
    }

    renderPreviews() {
        if (!this.previewContainer) return;
        this.previewContainer.innerHTML = "";
        
        // Render existing images from server
        this.existingImages.forEach((imgObj, index) => {
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
            removeBtn.onclick = () => {
                this.deletedImageIds.push(imgObj.id);
                this.existingImages.splice(index, 1);
                this.renderPreviews();
            };

            item.appendChild(img);
            item.appendChild(removeBtn);
            this.previewContainer.appendChild(item);
        });

        // Render new selected files
        this.selectedFiles.forEach((file, index) => {
            const item = document.createElement("div");
            item.className = "image-preview-item";

            const img = document.createElement("img");
            img.src = URL.createObjectURL(file);

            const removeBtn = document.createElement("button");
            removeBtn.type = "button";
            removeBtn.className = "remove-btn";
            removeBtn.innerHTML = "&times;";
            removeBtn.title = "Bỏ ảnh mới này";
            removeBtn.onclick = () => {
                this.selectedFiles.splice(index, 1);
                this.renderPreviews();
            };

            item.appendChild(img);
            item.appendChild(removeBtn);
            this.previewContainer.appendChild(item);
        });
    }

    clearErrors() {
        document.querySelectorAll('.error-msg').forEach(el => el.innerText = "");
    }

    openModal() {
        if (this.form) this.form.reset();
        this.selectedFiles = [];
        this.existingImages = [];
        this.deletedImageIds = [];
        this.isNameManuallyEdited = false;
        this.renderPreviews();
        
        // Reset hidden dropdown values
        this.dropdownKeys.forEach(key => {
            const hiddenInput = document.getElementById(this.prefix + this.capitalize(key));
            if (hiddenInput) hiddenInput.value = "";
        });

        this.initCustomDropdowns();

        const idInput = document.getElementById(this.prefix + "Id");
        if (idInput) idInput.value = "";
        if (this.nameInput) this.nameInput.value = "";
        
        const modalTitle = document.getElementById("modalTitle");
        if (modalTitle) modalTitle.innerText = "Thêm " + this.prefix.toUpperCase() + " mới";
        
        if (this.successAlert) this.successAlert.style.display = "none";
        this.clearErrors();
        
        if (!this.isNameManuallyEdited) {
            this.generateNameCallback();
        }
        
        if (this.modal) {
            const bsModal = bootstrap.Modal.getOrCreateInstance(this.modal);
            bsModal.show();
        }
    }

    closeModal() {
        if (this.modal) {
            const bsModal = bootstrap.Modal.getOrCreateInstance(this.modal);
            bsModal.hide();
        }
        this.closeAllDropdowns();
    }

    updateTableSTT() {
        if (!this.tbody) return;
        const rows = this.tbody.querySelectorAll("tr:not(#emptyRow)");
        rows.forEach((row, idx) => {
            const firstTd = row.querySelector("td");
            if (firstTd) {
                firstTd.innerText = idx + 1;
            }
        });
    }

    handleSuccess(item) {
        if (this.successAlert) {
            this.successAlert.style.display = "block";
            this.successAlert.innerText = "Lưu thành công " + this.prefix.toUpperCase() + ": " + item.name;
        }
        
        const modalTitle = document.getElementById("modalTitle");
        if (modalTitle) modalTitle.innerText = "Sửa " + this.prefix.toUpperCase() + " (đã lưu)";
        
        const idInput = document.getElementById(this.prefix + "Id");
        if (idInput) idInput.value = item.id; 
        
        this.selectedFiles = [];
        this.deletedImageIds = [];
        this.renderPreviews();

        if (this.emptyRow) this.emptyRow.style.display = 'none';

        let existingRow = document.getElementById(this.prefix + '-row-' + item.id);
        const rowHTML = this.renderRowHTML(item);

        if (existingRow) {
            existingRow.innerHTML = rowHTML;
        } else {
            const tr = document.createElement("tr");
            tr.id = this.prefix + '-row-' + item.id;
            tr.innerHTML = rowHTML;
            if (this.tbody) this.tbody.appendChild(tr);
        }

        this.updateTableSTT();
    }

    handleValidationErrors(errors) {
        for (const [key, value] of Object.entries(errors)) {
            const errorElement = document.getElementById("err-" + key);
            if (errorElement) {
                errorElement.innerText = value;
            } else if (key === 'general') {
                const generalErr = document.getElementById("err-general");
                if (generalErr) generalErr.innerText = value;
            }
        }
    }

    editItemModal(id) {
        fetch(this.apiEndpoint + '/' + id)
            .then(res => {
                if (!res.ok) throw new Error("Không tìm thấy dữ liệu");
                return res.json();
            })
            .then(data => {
                const item = data[this.prefix] || data; // handle both single object or wrapped
                this.existingImages = data.images || [];
                this.deletedImageIds = [];

                if (this.form) this.form.reset();
                this.selectedFiles = [];
                this.renderPreviews();
                this.clearErrors();
                if (this.successAlert) this.successAlert.style.display = "none";

                const modalTitle = document.getElementById("modalTitle");
                if (modalTitle) modalTitle.innerText = "Sửa " + this.prefix.toUpperCase() + " #" + item.id;
                
                const idInput = document.getElementById(this.prefix + "Id");
                if (idInput) idInput.value = item.id;
                
                if (this.nameInput) this.nameInput.value = item.name || "";
                
                // Populate common fields
                const priceInput = document.getElementById(this.prefix + "Price");
                if (priceInput) priceInput.value = item.price || "";
                
                const stockInput = document.getElementById(this.prefix + "Stock");
                if (stockInput) stockInput.value = item.stock || "";
                
                const imgUrlInput = document.getElementById(this.prefix + "ImageUrl");
                if (imgUrlInput) imgUrlInput.value = item.imageUrl || "";
                
                const descInput = document.getElementById(this.prefix + "Desc");
                if (descInput) descInput.value = item.description || "";

                // Populate custom dropdowns
                this.dropdownKeys.forEach(key => {
                    const val = item[key] || "";
                    if (val && !this.customDropdownData[key].includes(val)) {
                        this.customDropdownData[key].push(val);
                        this.saveCustomDropdownData();
                    }
                    const hiddenInput = document.getElementById(this.prefix + this.capitalize(key));
                    if (hiddenInput) hiddenInput.value = val;
                });
                this.initCustomDropdowns();
                
                // Populate specific fields via callback
                if (this.populateEditForm) {
                    this.populateEditForm(item);
                }

                this.isNameManuallyEdited = true;
                this.renderPreviews();
                if (this.modal) {
                    const bsModal = bootstrap.Modal.getOrCreateInstance(this.modal);
                    bsModal.show();
                }
            })
            .catch(err => {
                alert(err.message);
            });
    }

    bindEvents() {
        document.addEventListener("click", (e) => {
            if (!e.target.closest(".custom-select-container")) {
                this.closeAllDropdowns();
            }
        });

        if (this.nameInput) {
            this.nameInput.addEventListener("input", () => {
                this.isNameManuallyEdited = true;
            });
        }

        if (this.imagesInput) {
            this.imagesInput.addEventListener("change", (e) => {
                const files = Array.from(e.target.files);
                files.forEach(file => {
                    if (!this.selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
                        this.selectedFiles.push(file);
                    }
                });
                this.renderPreviews();
                this.imagesInput.value = "";
            });
        }

        if (this.openBtn) this.openBtn.onclick = () => this.openModal();
        // Custom close buttons are handled by data-bs-dismiss="modal", but we can keep these just in case
        if (this.closeBtn) this.closeBtn.onclick = () => this.closeModal();
        if (this.cancelBtn) this.cancelBtn.onclick = () => this.closeModal();


        if (this.form) {
            this.form.onsubmit = (e) => {
                e.preventDefault();
                this.clearErrors();
                if (this.successAlert) this.successAlert.style.display = "none";

                const descInput = document.getElementById(this.prefix + "Desc");
                if (descInput && (!descInput.value || descInput.value.trim() === "")) {
                    descInput.value = "không có mô tả";
                }

                const formData = new FormData(this.form);

                this.selectedFiles.forEach(file => {
                    formData.append("files", file);
                });

                this.deletedImageIds.forEach(id => {
                    formData.append("deletedImageIds", id);
                });

                fetch(this.apiEndpoint + '/save', {
                    method: 'POST',
                    body: formData
                })
                .then(async response => {
                    if (response.ok) {
                        const data = await response.json();
                        this.handleSuccess(data);
                    } else {
                        const errorData = await response.json();
                        this.handleValidationErrors(errorData);
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    const generalErr = document.getElementById("err-general");
                    if (generalErr) generalErr.innerText = "Đã xảy ra lỗi hệ thống!";
                });
            };
        }
    }
}
