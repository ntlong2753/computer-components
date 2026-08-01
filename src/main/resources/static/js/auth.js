document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById('loginForm');
    const errorAlert = document.getElementById('errorAlert');
    const serverErrorAlert = document.getElementById('serverErrorAlert');

    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault(); // Ngăn chặn form submit mặc định để không bị reset trang
            
            // Ẩn thông báo lỗi server (nếu có)
            if (serverErrorAlert) serverErrorAlert.style.display = 'none';
            
            const formData = new URLSearchParams(new FormData(loginForm));
            
            fetch('/login', {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw response;
                }
            })
            .then(data => {
                if (data.success) {
                    // Thành công, chuyển hướng về trang chủ
                    window.location.href = '/';
                }
            })
            .catch(errorResponse => {
                if (errorResponse.status === 401) {
                    // Lỗi 401 (Unauthorized) - Hiển thị lỗi, form không bị clear
                    if (errorAlert) {
                        errorAlert.style.display = 'block';
                        errorAlert.textContent = 'Tài khoản hoặc mật khẩu không chính xác!';
                    }
                } else {
                    console.error("Lỗi đăng nhập:", errorResponse);
                }
            });
        });
    }
});
