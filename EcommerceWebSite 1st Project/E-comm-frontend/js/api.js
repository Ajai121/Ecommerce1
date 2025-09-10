export const API_BASE = "http://localhost:8080";

export async function apiGet(path){
  const res = await fetch(`${API_BASE}${path}`);
  if(!res.ok) throw new Error(`GET ${path} → ${res.status}`);
  return res.json();
}

export async function apiSend(method, path, body){
  const opts = { method, headers: { "Content-Type":"application/json" } };
  if(body) opts.body = JSON.stringify(body);
  const res = await fetch(`${API_BASE}${path}`, opts);
  if(!res.ok){
    const text = await res.text().catch(()=> "");
    throw new Error(`${method} ${path} → ${res.status} ${text}`);
  }
  try { return await res.json(); } catch { return true; }
}

// ---- Product image helper (backend-driven) ----
export function getProductImage(product){
  if(product && product.imageUrl && product.imageUrl.trim()){
    return product.imageUrl;
  }
  return "assets/default.svg"; // fallback placeholder
}

// ---- User helpers ----
export function getUserId(){
  let uid = localStorage.getItem("userId");
  if(!uid){ uid = "1"; localStorage.setItem("userId", uid); }
  return uid;
}

export function setUserId(uid){
  localStorage.setItem("userId", String(uid));
}

// ---- Currency helper ----
export function money(v){
  const n = Number(v || 0);
  return new Intl.NumberFormat("en-IN", { 
    style: "currency", 
    currency: "INR", 
    maximumFractionDigits: 2 
  }).format(n);
}