
const API_BASE = "http://localhost:8080/api";

async function fetchCategories() {
    const res = await fetch(`${API_BASE}/category/allCategory`);
    const categories = await res.json();
    const container = document.getElementById("categories");
    container.innerHTML = categories.map(cat => 
        `<button onclick="fetchProductsByCategory('${cat.name}')">${cat.name}</button>`
    ).join(" ");
}

async function fetchProducts() {
    const res = await fetch(`${API_BASE}/product/allProduct`);
    const products = await res.json();
    renderProducts(products);
}

async function fetchProductsByCategory(category) {
    const res = await fetch(`${API_BASE}/product/category/${category}`);
    const products = await res.json();
    renderProducts(products);
}

function getProductImage(productCode) {
    const images = JSON.parse(localStorage.getItem("productImages") || "{}");
    return images[productCode] || "assets/default.jpg";
}

function renderProducts(products) {
    const container = document.getElementById("product-grid");
    container.innerHTML = products.map(p => `
        <div class="product-card">
            <img src="${getProductImage(p.productCode)}" alt="${p.productName}">
            <div class="info">
                <h3>${p.productName}</h3>
                <p>₹${p.productPrice}</p>
                <button onclick="viewProduct('${p.productCode}')">View Details</button>
            </div>
        </div>
    `).join("");
}

function viewProduct(code) {
    window.location.href = `product.html?code=${code}`;
}

fetchCategories();
fetchProducts();
