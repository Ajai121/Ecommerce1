// js/layout.js

// --- AUTH HELPERS ---
export function getAuth() {
  return {
    userId: localStorage.getItem("userId") || null,
    token: localStorage.getItem("token") || null,
    userName: localStorage.getItem("userName") || null   // ✅ add username
  };
}

export function isLoggedIn() {
  return !!getAuth().userId;
}

export function logout() {
  localStorage.removeItem("userId");
  localStorage.removeItem("token");
  localStorage.removeItem("userName"); // ✅ clear username too
  location.href = "index.html";
}

// --- HEADER ---
export function renderHeader(active = "") {
  const auth = getAuth();

  return `
  <header class="header" style="position:fixed; top:0; width:100%; z-index:100; background:#fff; box-shadow:0 2px 5px rgba(0,0,0,0.1);">
    <div class="header-inner container">
      <a href="index.html" class="logo"><span class="pill">neo</span>shop</a>
      
      <button class="nav-toggle" id="navToggle">☰</button>

      <div class="search">
        <input id="q" placeholder="Search products..." />
        <button id="qBtn">Search</button>
      </div>

      <nav class="nav" id="navMenu">
        <a class="badge ${active==='home'?'chip active':''}" href="index.html">Home</a>
        <a class="badge ${active==='cart'?'chip active':''}" href="cart.html">Cart</a>
        <a class="badge ${active==='orders'?'chip active':''}" href="orders.html">Orders</a>
        <a class="badge ${active==='admin'?'chip active':''}" href="admin.html">Admin</a>
        ${
          auth.userId ? `
            <span class="badge">User: ${auth.userName || auth.userId}</span>
            <button id="logoutBtn" class="btn ghost small">Logout</button>
          ` : `
            <a class="badge ${active==='auth'?'chip active':''}" href="auth.html">Login / Register</a>
          `
        }
      </nav>
    </div>
  </header>`;
}

export function mountHeader(active) {
  const h = document.getElementById("app-header");
  h.innerHTML = renderHeader(active);

  // Nav toggle
  const toggle = document.getElementById("navToggle");
  const menu = document.getElementById("navMenu");
  if (toggle && menu) {
    toggle.addEventListener("click", () => {
      menu.classList.toggle("open");
    });
  }

  // Logout
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
  }
}

// --- FOOTER ---
export function renderFooter() {
  return `
  <footer class="footer container" style="padding:20px 0; margin-top:40px; border-top:1px solid #ddd; font-size:14px; color:#444;">
    <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px,1fr)); gap:20px;">
      
      <div>
        <h4 style="margin-bottom:10px;">About NeoShop</h4>
        <p>Your one-stop online store for fashion, electronics, and home essentials. 
        Enjoy secure payments, fast delivery, and 24/7 customer support.</p>
      </div>

      <div>
        <h4 style="margin-bottom:10px;">Customer Support</h4>
        <p><a href="#" style="color:#0077ff; text-decoration:none;">Help Center</a></p>
        <p><a href="#" style="color:#0077ff; text-decoration:none;">Returns & Refunds</a></p>
        <p><a href="#" style="color:#0077ff; text-decoration:none;">Shipping Info</a></p>
      </div>

      <div>
        <h4 style="margin-bottom:10px;">Contact Us</h4>
        <p>Email: support@neoshop.com</p>
        <p>Phone: +91 98765 43210</p>
        <p>Follow us:
          <a href="#" style="color:#0077ff; text-decoration:none; margin-left:6px;">Facebook</a> |
          <a href="#" style="color:#0077ff; text-decoration:none; margin-left:6px;">Instagram</a>
        </p>
      </div>
    </div>

    <div style="text-align:center; margin-top:20px; font-size:13px; color:#666;">
      © ${new Date().getFullYear()} NeoShop • All rights reserved.
    </div>
  </footer>`;
}


export function mountFooter() {
  document.getElementById("app-footer").innerHTML = renderFooter();
}

// --- SEARCH ---
export function bindSearch(handler) {
  const q = document.getElementById("q");
  const btn = document.getElementById("qBtn");
  if (!q || !btn) return;
  btn.addEventListener("click", () => handler(q.value.trim()));
  q.addEventListener("keydown", (e) => {
    if (e.key === 'Enter') handler(q.value.trim());
  });
}
