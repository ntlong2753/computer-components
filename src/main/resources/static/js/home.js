document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            if (this.getAttribute('href') === '#') return;
            e.preventDefault();
            let target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
        });
    });
});

function addToCart(productId) {
    const formData = new URLSearchParams();
    formData.append("productId", productId);
    formData.append("quantity", 1);

    fetch('/api/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update cart badge
            const badges = document.querySelectorAll('a[href="/cart"] .badge');
            badges.forEach(badge => {
                badge.textContent = data.cartItemCount;
            });
            alert(data.message);
        } else {
            alert("Có lỗi xảy ra: " + (data.error || "Không thể thêm vào giỏ hàng"));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Có lỗi xảy ra khi thêm vào giỏ hàng.");
    });
}
