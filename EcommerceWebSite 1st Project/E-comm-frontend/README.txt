
# neoshop — Vanilla HTML/CSS/JS front-end

This is a fully functional front-end wired to your API at `http://localhost:8080`.

## Pages
- `index.html` — Home with categories & product grid
- `product.html` — Product details (opens via `?code=...`)
- `cart.html` — Cart with update/remove/clear
- `checkout.html` — Place order
- `admin.html` — Add/Update/Delete products & manage *front-end-only* images

## Images
Your backend doesn't store images. Use **Admin → Front-End Images** to bind:
- Image URL → Product code or ID (stored in localStorage)
- Image URL → Category name (fallback when a product image isn't set)

A default placeholder exists at `assets/default.svg`.

## User ID
Set the current user via the top-right input in the header (stored in localStorage).

## Running
Open `index.html` with a local server (due to ES modules):
- Python: `python3 -m http.server 5173` then visit `http://localhost:5173/`
- Node: `npx serve` or any static server

Make sure your backend is running at `http://localhost:8080`.
