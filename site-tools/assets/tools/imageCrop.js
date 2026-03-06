import {
  bindDropzone,
  canvasToBlob,
  clamp,
  downloadBlob,
  formatBytes,
  guessExtFromMime,
  isImageFile,
  loadImageFromFile,
  revokeObjectUrl,
  safeBaseName,
} from "./common.js";

const els = {
  dropzone: document.getElementById("dropzone"),
  fileInput: document.getElementById("fileInput"),
  previewWrap: document.getElementById("previewWrap"),
  canvas: document.getElementById("canvas"),
  srcMeta: document.getElementById("srcMeta"),
  cropMeta: document.getElementById("cropMeta"),
  outMeta: document.getElementById("outMeta"),
  outType: document.getElementById("outType"),
  qualityField: document.getElementById("qualityField"),
  quality: document.getElementById("quality"),
  qualityNum: document.getElementById("qualityNum"),
  qualityText: document.getElementById("qualityText"),
  btnExport: document.getElementById("btnExport"),
  btnResetCrop: document.getElementById("btnResetCrop"),
  btnReset: document.getElementById("btnReset"),
  toast: document.getElementById("toast"),
};

let state = {
  file: null,
  img: null,
  imgUrl: null,
  // scale: image -> canvas
  scale: 1,
  offsetX: 0,
  offsetY: 0,
  // crop rect in canvas coords
  crop: null, // {x,y,w,h}
  dragging: null, // {mode, startX, startY, crop0}
};

function showToast(message, { error = false } = {}) {
  els.toast.style.display = "block";
  els.toast.textContent = message;
  els.toast.classList.toggle("is-error", !!error);
}

function hideToast() {
  els.toast.style.display = "none";
  els.toast.textContent = "";
  els.toast.classList.remove("is-error");
}

function setButtons() {
  const hasImg = !!state.img;
  const hasCrop = !!state.crop && state.crop.w >= 2 && state.crop.h >= 2;
  els.btnExport.disabled = !(hasImg && hasCrop);
  els.btnResetCrop.disabled = !(hasImg && hasCrop);
}

function updateQualityVisibility() {
  const mime = els.outType.value;
  const show = mime === "image/jpeg" || mime === "image/webp";
  els.qualityField.style.display = show ? "" : "none";
}

function syncQualityUI(v) {
  const q = clamp(Number(v) || 0.92, 0.1, 1);
  els.quality.value = String(q);
  els.qualityNum.value = String(q);
  els.qualityText.textContent = q.toFixed(2);
}

function cleanupImage() {
  if (state.imgUrl) revokeObjectUrl(state.imgUrl);
  state.imgUrl = null;
  state.img = null;
}

function resetAll() {
  hideToast();
  cleanupImage();
  state.file = null;
  state.scale = 1;
  state.offsetX = 0;
  state.offsetY = 0;
  state.crop = null;
  state.dragging = null;

  els.fileInput.value = "";
  els.previewWrap.style.display = "none";
  els.srcMeta.textContent = "-";
  els.cropMeta.textContent = "-";
  els.outMeta.textContent = "-";
  els.outType.value = "image/png";
  updateQualityVisibility();
  syncQualityUI(0.92);
  els.canvas.width = 1;
  els.canvas.height = 1;
  setButtons();
}

function resetCrop() {
  state.crop = null;
  state.dragging = null;
  els.cropMeta.textContent = "-";
  els.outMeta.textContent = "-";
  render();
  setButtons();
}

function computeCanvasLayout(img) {
  const maxW = 900;
  const maxH = 560;
  const srcW = img.naturalWidth || img.width;
  const srcH = img.naturalHeight || img.height;
  const scale = Math.min(1, maxW / srcW, maxH / srcH);
  const cw = Math.max(1, Math.floor(srcW * scale));
  const ch = Math.max(1, Math.floor(srcH * scale));
  return { scale, cw, ch };
}

function render() {
  const img = state.img;
  const canvas = els.canvas;
  const ctx = canvas.getContext("2d");
  if (!ctx) return;

  ctx.clearRect(0, 0, canvas.width, canvas.height);
  if (!img) return;

  ctx.imageSmoothingEnabled = true;
  ctx.imageSmoothingQuality = "high";
  ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

  // draw crop rect overlay
  if (state.crop) {
    const { x, y, w, h } = state.crop;
    ctx.save();
    // dim outside
    ctx.fillStyle = "rgba(2,6,23,0.45)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.clearRect(x, y, w, h);

    // border
    ctx.strokeStyle = "rgba(59,130,246,0.95)";
    ctx.lineWidth = 2;
    ctx.strokeRect(x + 1, y + 1, Math.max(0, w - 2), Math.max(0, h - 2));

    // handles
    const r = 6;
    const corners = [
      [x, y],
      [x + w, y],
      [x, y + h],
      [x + w, y + h],
    ];
    ctx.fillStyle = "#ffffff";
    ctx.strokeStyle = "rgba(59,130,246,0.95)";
    for (const [cx, cy] of corners) {
      ctx.beginPath();
      ctx.arc(cx, cy, r, 0, Math.PI * 2);
      ctx.fill();
      ctx.stroke();
    }

    ctx.restore();
  }
}

function setCropMeta() {
  if (!state.img || !state.crop) {
    els.cropMeta.textContent = "-";
    return;
  }
  const imgW = state.img.naturalWidth || state.img.width;
  const imgH = state.img.naturalHeight || state.img.height;
  const r = cropCanvasToImageRect();
  els.cropMeta.textContent = `${Math.round(r.w)}×${Math.round(r.h)}（原图坐标） · 画布 ${Math.round(
    state.crop.w,
  )}×${Math.round(state.crop.h)}`;
  // guard: show bounds if needed
  void imgW;
  void imgH;
}

function cropCanvasToImageRect() {
  const c = state.crop;
  if (!c || !state.img) return null;
  const inv = 1 / state.scale;
  return {
    x: clamp(c.x * inv, 0, (state.img.naturalWidth || state.img.width) - 1),
    y: clamp(c.y * inv, 0, (state.img.naturalHeight || state.img.height) - 1),
    w: clamp(c.w * inv, 1, state.img.naturalWidth || state.img.width),
    h: clamp(c.h * inv, 1, state.img.naturalHeight || state.img.height),
  };
}

function getPointerPos(evt) {
  const rect = els.canvas.getBoundingClientRect();
  const x = evt.clientX - rect.left;
  const y = evt.clientY - rect.top;
  return { x, y };
}

function hitTest(pos) {
  const c = state.crop;
  if (!c) return { mode: "new" };
  const { x, y, w, h } = c;
  const r = 10;
  const near = (px, py, tx, ty) => Math.hypot(px - tx, py - ty) <= r;
  const corners = {
    nw: { x: x, y: y },
    ne: { x: x + w, y: y },
    sw: { x: x, y: y + h },
    se: { x: x + w, y: y + h },
  };
  for (const [k, v] of Object.entries(corners)) {
    if (near(pos.x, pos.y, v.x, v.y)) return { mode: `resize-${k}` };
  }
  const inside = pos.x >= x && pos.x <= x + w && pos.y >= y && pos.y <= y + h;
  if (inside) return { mode: "move" };
  return { mode: "new" };
}

function normalizeRect(x0, y0, x1, y1) {
  const x = Math.min(x0, x1);
  const y = Math.min(y0, y1);
  const w = Math.abs(x1 - x0);
  const h = Math.abs(y1 - y0);
  return { x, y, w, h };
}

function clampCropToCanvas(crop) {
  const cw = els.canvas.width;
  const ch = els.canvas.height;
  const x = clamp(crop.x, 0, cw);
  const y = clamp(crop.y, 0, ch);
  const w = clamp(crop.w, 0, cw - x);
  const h = clamp(crop.h, 0, ch - y);
  return { x, y, w, h };
}

function onPointerDown(evt) {
  if (!state.img) return;
  hideToast();
  els.canvas.setPointerCapture(evt.pointerId);
  const p = getPointerPos(evt);
  const { mode } = hitTest(p);
  state.dragging = {
    mode,
    startX: p.x,
    startY: p.y,
    crop0: state.crop ? { ...state.crop } : null,
  };
  if (mode === "new") {
    state.crop = { x: p.x, y: p.y, w: 0, h: 0 };
  }
  render();
}

function onPointerMove(evt) {
  if (!state.img || !state.dragging) return;
  const p = getPointerPos(evt);
  const d = state.dragging;
  const cw = els.canvas.width;
  const ch = els.canvas.height;

  if (d.mode === "new") {
    const r = normalizeRect(d.startX, d.startY, p.x, p.y);
    state.crop = clampCropToCanvas(r);
  } else if (d.mode === "move" && d.crop0) {
    const dx = p.x - d.startX;
    const dy = p.y - d.startY;
    const x = clamp(d.crop0.x + dx, 0, cw - d.crop0.w);
    const y = clamp(d.crop0.y + dy, 0, ch - d.crop0.h);
    state.crop = { ...d.crop0, x, y };
  } else if (d.mode.startsWith("resize-") && d.crop0) {
    const c0 = d.crop0;
    const minSize = 8;
    let x0 = c0.x;
    let y0 = c0.y;
    let x1 = c0.x + c0.w;
    let y1 = c0.y + c0.h;
    const key = d.mode.replace("resize-", "");
    if (key.includes("n")) y0 = clamp(p.y, 0, y1 - minSize);
    if (key.includes("s")) y1 = clamp(p.y, y0 + minSize, ch);
    if (key.includes("w")) x0 = clamp(p.x, 0, x1 - minSize);
    if (key.includes("e")) x1 = clamp(p.x, x0 + minSize, cw);
    const r = normalizeRect(x0, y0, x1, y1);
    state.crop = clampCropToCanvas(r);
  }

  els.outMeta.textContent = "-";
  setCropMeta();
  setButtons();
  render();
}

function onPointerUp(evt) {
  if (!state.dragging) return;
  try {
    els.canvas.releasePointerCapture(evt.pointerId);
  } catch {
    // ignore
  }
  state.dragging = null;
  const c = state.crop;
  if (c && (c.w < 2 || c.h < 2)) state.crop = null;
  setCropMeta();
  setButtons();
  render();
}

async function setFile(file) {
  hideToast();
  if (!file) return;
  if (!isImageFile(file)) {
    showToast("仅支持图片文件（JPG / PNG / WebP 等）。", { error: true });
    return;
  }
  cleanupImage();
  state.file = file;
  const { img, objectUrl } = await loadImageFromFile(file);
  state.img = img;
  state.imgUrl = objectUrl;
  els.previewWrap.style.display = "";
  els.srcMeta.textContent = `${file.name} · ${img.naturalWidth}×${img.naturalHeight} · ${formatBytes(
    file.size,
  )}`;
  els.outMeta.textContent = "-";

  const { scale, cw, ch } = computeCanvasLayout(img);
  state.scale = scale;
  els.canvas.width = cw;
  els.canvas.height = ch;

  // default crop: centered 80%
  state.crop = {
    x: Math.round(cw * 0.1),
    y: Math.round(ch * 0.1),
    w: Math.round(cw * 0.8),
    h: Math.round(ch * 0.8),
  };
  setCropMeta();
  render();
  setButtons();
}

async function exportAndDownload() {
  hideToast();
  if (!state.img || !state.file || !state.crop) {
    showToast("请先上传图片并选择裁剪区域。", { error: true });
    return;
  }
  const r = cropCanvasToImageRect();
  if (!r || r.w < 2 || r.h < 2) {
    showToast("裁剪区域太小，请重新选择。", { error: true });
    return;
  }

  const mime = els.outType.value;
  const q = clamp(Number(els.quality.value) || 0.92, 0.1, 1);
  try {
    const outCanvas = document.createElement("canvas");
    outCanvas.width = Math.max(1, Math.round(r.w));
    outCanvas.height = Math.max(1, Math.round(r.h));
    const ctx = outCanvas.getContext("2d");
    if (!ctx) throw new Error("无法获取 Canvas 上下文。");
    ctx.imageSmoothingEnabled = true;
    ctx.imageSmoothingQuality = "high";
    ctx.drawImage(
      state.img,
      r.x,
      r.y,
      r.w,
      r.h,
      0,
      0,
      outCanvas.width,
      outCanvas.height,
    );
    const blob = await canvasToBlob(outCanvas, {
      type: mime,
      quality: mime === "image/jpeg" || mime === "image/webp" ? q : undefined,
    });
    const ext = guessExtFromMime(mime);
    const base = safeBaseName(state.file.name);
    const filename = `${base}.crop.${ext}`;
    els.outMeta.textContent = `${filename} · ${outCanvas.width}×${outCanvas.height} · ${formatBytes(
      blob.size,
    )}`;
    downloadBlob(blob, filename);
  } catch (e) {
    showToast(e?.message || "导出失败，请重试。", { error: true });
  }
}

function init() {
  updateQualityVisibility();
  syncQualityUI(els.quality.value);
  setButtons();

  bindDropzone(els.dropzone, {
    onFile: (file) => setFile(file).catch((e) => showToast(e.message, { error: true })),
  });
  els.dropzone.addEventListener("click", () => els.fileInput.click());
  els.dropzone.addEventListener("keydown", (e) => {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      els.fileInput.click();
    }
  });
  els.fileInput.addEventListener("change", () => {
    const file = els.fileInput.files?.[0] || null;
    if (!file) return;
    setFile(file).catch((e) => showToast(e.message, { error: true }));
  });

  els.canvas.addEventListener("pointerdown", onPointerDown);
  els.canvas.addEventListener("pointermove", onPointerMove);
  els.canvas.addEventListener("pointerup", onPointerUp);
  els.canvas.addEventListener("pointercancel", onPointerUp);

  els.outType.addEventListener("change", () => {
    updateQualityVisibility();
  });
  els.quality.addEventListener("input", () => syncQualityUI(els.quality.value));
  els.qualityNum.addEventListener("input", () => syncQualityUI(els.qualityNum.value));

  els.btnExport.addEventListener("click", () => exportAndDownload());
  els.btnResetCrop.addEventListener("click", () => resetCrop());
  els.btnReset.addEventListener("click", () => resetAll());

  window.addEventListener("beforeunload", () => cleanupImage());
}

init();

