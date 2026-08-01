// Toggle Edit Mode Logic
const originalValues = {};

function enableEdit() {
    // Save original values in case of cancel
    document.querySelectorAll('.editable-field').forEach(el => {
        originalValues[el.id] = el.value;
        el.removeAttribute('readonly');
        el.classList.add('bg-white');
        
        // Clear masked values so user can type easily
        if (el.value.includes('*')) {
            el.value = '';
        }
    });
    
    document.getElementById('actionButtons').classList.add('d-none');
    document.getElementById('saveButtons').classList.remove('d-none');
    document.getElementById('avatarUploadBtn').style.display = 'block';
}

// Intercept form submission to restore empty fields
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('profileForm');
    if (form) {
        form.addEventListener('submit', function() {
            document.querySelectorAll('.editable-field').forEach(el => {
                if (el.value.trim() === '' && originalValues[el.id] !== undefined) {
                    el.value = originalValues[el.id];
                }
            });
        });
    }
});

function cancelEdit() {
    // Restore original values
    document.querySelectorAll('.editable-field').forEach(el => {
        if(originalValues[el.id] !== undefined) {
            el.value = originalValues[el.id];
        }
        el.setAttribute('readonly', true);
        el.classList.remove('bg-white');
    });
    
    document.getElementById('actionButtons').classList.remove('d-none');
    document.getElementById('saveButtons').classList.add('d-none');
    document.getElementById('avatarUploadBtn').style.display = 'none';
}

function previewImage(input) {
    if (input.files && input.files[0]) {
        var file = input.files[0];
        if (file.size > 10 * 1024 * 1024) {
            alert('Ảnh đại diện có dung lượng không được quá 10MB');
            input.value = '';
            return;
        }
        var reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('avatarPreview').src = e.target.result;
        }
        reader.readAsDataURL(file);
    }
}
