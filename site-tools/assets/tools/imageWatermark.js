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
  outMeta: document.getElementById("outMeta"),
  wmText: document.getElementById("wmText"),
  fontSize: document.getElementById("fontSize"),
  fontSizeNum: document.getElementById("fontSizeNum"),
  fontSizeText: document.getElementById("fontSizeText"),
  color: document.getElementById("color"),
  opacity: document.getElementById("opacity"),
  opacityNum: document.getElementById("opacityNum"),
  opacityText: document.getElementById("opacityText"),
  position: document.getElementById("position"),
  margin: document.getElementById("margin"),
  marginNum: document.getElementById("marginNum"),
  marginText: document.getElementById("marginText"),
  wmImgInput: document.getElementById("wmImgInput"),
  wmScaleField: document.getElementById("wmScaleField"),
  wmScale: document.getElementById("wmScale"),
  wmScaleNum: document.getElementById("wmScaleNum"),
  wmScaleText: document.getElementById("wmScaleText"),
  outType: document.getElementById("outType"),
  qualityField: document.getElementById("qualityField"),
  quality: document.getElementById("quality"),
  qualityNum: document.getElementById("qualityNum"),
  qualityText: document.getElementById("qualityText"),
  btnRender: document.getElementById("btnRender"),
  btnExport: document.getElementById("btnExport"),
  btnReset: document.getElementById("btnReset"),
  toast: document.getElementById("toast"),
};

let state = {
  file: null,
  img: null,
  imgUrl: null,
  wmFile: null,
  wmImg: null,
  wmUrl: null,
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

function cleanupBaseImage() {
  if (state.imgUrl) revokeObjectUrl(state.imgUrl);
  state.imgUrl = null;
  state.img = null;
}

function cleanupWmImage() {
  if (state.wmUrl) revokeObjectUrl(state.wmUrl);
  state.wmUrl = null;
  state.wmImg = null;
  state.wmFile = null;
  els.wmImgInput.value = "";
  els.wmScaleField.style.display = "none";
}

function resetAll() {
  hideToast();
  cleanupWmImage();
  cleanupBaseImage();
  state.file = null;
  els.fileInput.value = "";
  els.previewWrap.style.display = "none";
  els.srcMeta.textContent = "-";
  els.outMeta.textContent = "-";
  els.wmText.value = "© 工具小站";
  syncNumRange(els.fontSize, els.fontSizeNum, els.fontSizeText, 32, 0);
  els.color.value = "#ffffff";
  syncNumRange(els.opacity, els.opacityNum, els.opacityText, 0.35, 2);
  els.position.value = "center";
  syncNumRange(els.margin, els.marginNum, els.marginText, 16, 0);
  syncNumRange(els.wmScale, els.wmScaleNum, els.wmScaleText, 0.2, 2);
  els.outType.value = "image/png";
  syncNumRange(els.quality, els.qualityNum, els.qualityText, 0.92, 2);
  updateQualityVisibility();
  setButtons({ canRender: false, canExport: false });
}

function setButtons({ canRender, canExport } = {}) {
  els.btnRender.disabled = !canRender;
  els.btnExport.disabled = !canExport;
}

function syncNumRange(rangeEl, numEl, textEl, value, digits = 2) {
  const v = typeof value === "number" ? value : Number(value);
  rangeEl.value = String(v);
  numEl.value = String(v);
  textEl.textContent = digits ? v.toFixed(digits) : String(Math.round(v));
}

function updateQualityVisibility() {
  const mime = els.outType.value;
  const show = mime === "image/jpeg" || mime === "image/webp";
  els.qualityField.style.display = show ? "" : "none";
}

async function setBaseFile(file) {
  hideToast();
  if (!file) return;
  if (!isImageFile(file)) {
    showToast("仅支持图片文件（JPG / PNG / WebP 等）。", { error: true });
    return;
  }

  cleanupBaseImage();
  state.file = file;
  const { img, objectUrl } = await loadImageFromFile(file);
  state.img = img;
  state.imgUrl = objectUrl;
  els.previewWrap.style.display = "";
  els.srcMeta.textContent = `${file.name} · ${img.naturalWidth}×${img.naturalHeight} · ${formatBytes(
    file.size,
  )}`;
  els.outMeta.textContent = "-";
  setButtons({ canRender: true, canExport: false });
  renderPreview();
}

async function setWatermarkFile(file) {
  hideToast();
  if (!file) {
    cleanupWmImage();
    renderPreview();
    return;
  }
  if (!isImageFile(file)) {
    showToast("水印图片必须是图片格式。", { error: true });
    return;
  }
  cleanupWmImage();
  state.wmFile = file;
  const { img, objectUrl } = await loadImageFromFile(file);
  state.wmImg = img;
  state.wmUrl = objectUrl;
  els.wmScaleField.style.display = "";
  renderPreview();
}

function computeAnchor({ w, h, position, margin }) {
  const m = margin;
  const map = {
    "top-left": { x: m, y: m, ax: 0, ay: 0 },
    top: { x: w / 2, y: m, ax: 0.5, ay: 0 },
    "top-right": { x: w - m, y: m, ax: 1, ay: 0 },
    left: { x: m, y: h / 2, ax: 0, ay: 0.5 },
    center: { x: w / 2, y: h / 2, ax: 0.5, ay: 0.5 },
    right: { x: w - m, y: h / 2, ax: 1, ay: 0.5 },
    "bottom-left": { x: m, y: h - m, ax: 0, ay: 1 },
    bottom: { x: w / 2, y: h - m, ax: 0.5, ay: 1 },
    "bottom-right": { x: w - m, y: h - m, ax: 1, ay: 1 },
  };
  return map[position] || map.center;
}

function renderToCanvas(canvas, { forExport = false } = {}) {
  const img = state.img;
  if (!img) throw new Error("请先上传底图。");

  const maxPreviewWidth = 900;
  const srcW = img.naturalWidth || img.width;
  const srcH = img.naturalHeight || img.height;

  let targetW = srcW;
  let targetH = srcH;
  if (!forExport && srcW > maxPreviewWidth) {
    targetW = maxPreviewWidth;
    targetH = Math.round((targetW / srcW) * srcH);
  }

  canvas.width = Math.max(1, Math.floor(targetW));
  canvas.height = Math.max(1, Math.floor(targetH));

  const ctx = canvas.getContext("2d");
  if (!ctx) throw new Error("无法获取 Canvas 上下文。");
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.imageSmoothingEnabled = true;
  ctx.imageSmoothingQuality = "high";
  ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

  const text = String(els.wmText.value || "").trim();
  const fontSize = clamp(Number(els.fontSize.value) || 32, 10, 120);
  const color = els.color.value || "#ffffff";
  const opacity = clamp(Number(els.opacity.value) || 0.35, 0.05, 1);
  const position = els.position.value || "center";
  const margin = clamp(Number(els.margin.value) || 16, 0, 120);

  const anchor = computeAnchor({ w: canvas.width, h: canvas.height, position, margin });

  ctx.save();
  ctx.globalAlpha = opacity;

  // 图片水印（可选）
  if (state.wmImg) {
    const scale = clamp(Number(els.wmScale.value) || 0.2, 0.05, 0.6);
    const wmW = Math.max(1, Math.round(canvas.width * scale));
    const wmH = Math.max(1, Math.round((wmW / (state.wmImg.naturalWidth || 1)) * (state.wmImg.naturalHeight || 1)));
    const x = Math.round(anchor.x - wmW * anchor.ax);
    const y = Math.round(anchor.y - wmH * anchor.ay);
    ctx.drawImage(state.wmImg, x, y, wmW, wmH);
  }

  // 文字水印（可选）
  if (text) {
    ctx.fillStyle = color;
    ctx.textBaseline = "alphabetic";
    ctx.font = `600 ${fontSize}px system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif`;
    ctx.shadowColor = "rgba(0,0,0,0.25)";
    ctx.shadowBlur = Math.max(0, Math.round(fontSize / 8));
    ctx.shadowOffsetX = 0;
    ctx.shadowOffsetY = Math.max(0, Math.round(fontSize / 10));

    const metrics = ctx.measureText(text);
    const textW = metrics.width;
    const textH = fontSize; // approximate
    const x = Math.round(anchor.x - textW * anchor.ax);
    const y = Math.round(anchor.y + textH * (1 - anchor.ay) - Math.round(fontSize * 0.25));
    ctx.fillText(text, x, y);
  }

  ctx.restore();
}

function renderPreview() {
  hideToast();
  if (!state.img) return;
  try {
    renderToCanvas(els.canvas, { forExport: false });
    setButtons({ canRender: true, canExport: true });
    els.outMeta.textContent = "-";
  } catch (e) {
    setButtons({ canRender: !!state.img, canExport: false });
    showToast(e?.message || "预览失败。", { error: true });
  }
}

async function exportAndDownload() {
  hideToast();
  if (!state.img || !state.file) {
    showToast("请先上传底图。", { error: true });
    return;
  }

  const mime = els.outType.value;
  const q = clamp(Number(els.quality.value) || 0.92, 0.1, 1);

  try {
    const exportCanvas = document.createElement("canvas");
    renderToCanvas(exportCanvas, { forExport: true });
    const blob = await canvasToBlob(exportCanvas, {
      type: mime,
      quality: mime === "image/jpeg" || mime === "image/webp" ? q : undefined,
    });
    const ext = guessExtFromMime(mime);
    const base = safeBaseName(state.file.name);
    const filename = `${base}.watermark.${ext}`;
    els.outMeta.textContent = `${filename} · ${exportCanvas.width}×${exportCanvas.height} · ${formatBytes(
      blob.size,
    )}`;
    downloadBlob(blob, filename);
  } catch (e) {
    showToast(e?.message || "导出失败，请重试。", { error: true });
  }
}

function init() {
  els.wmText.value = "© 工具小站";
  syncNumRange(els.fontSize, els.fontSizeNum, els.fontSizeText, 32, 0);
  syncNumRange(els.opacity, els.opacityNum, els.opacityText, 0.35, 2);
  syncNumRange(els.margin, els.marginNum, els.marginText, 16, 0);
  syncNumRange(els.wmScale, els.wmScaleNum, els.wmScaleText, 0.2, 2);
  syncNumRange(els.quality, els.qualityNum, els.qualityText, 0.92, 2);
  updateQualityVisibility();
  setButtons({ canRender: false, canExport: false });

  bindDropzone(els.dropzone, {
    onFile: (file) => setBaseFile(file).catch((e) => showToast(e.message, { error: true })),
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
    setBaseFile(file).catch((e) => showToast(e.message, { error: true }));
  });

  const rerenderEvents = ["input", "change"];
  for (const ev of rerenderEvents) {
    els.wmText.addEventListener(ev, () => renderPreview());
    els.color.addEventListener(ev, () => renderPreview());
    els.position.addEventListener(ev, () => renderPreview());
  }

  const bindNumPair = (rangeEl, numEl, textEl, digits, onChange) => {
    const sync = (v) => syncNumRange(rangeEl, numEl, textEl, v, digits);
    rangeEl.addEventListener("input", () => {
      sync(rangeEl.value);
      onChange();
    });
    numEl.addEventListener("input", () => {
      sync(numEl.value);
      onChange();
    });
  };

  bindNumPair(els.fontSize, els.fontSizeNum, els.fontSizeText, 0, renderPreview);
  bindNumPair(els.opacity, els.opacityNum, els.opacityText, 2, renderPreview);
  bindNumPair(els.margin, els.marginNum, els.marginText, 0, renderPreview);
  bindNumPair(els.wmScale, els.wmScaleNum, els.wmScaleText, 2, renderPreview);
  bindNumPair(els.quality, els.qualityNum, els.qualityText, 2, () => {});

  els.wmImgInput.addEventListener("change", () => {
    const file = els.wmImgInput.files?.[0] || null;
    setWatermarkFile(file).catch((e) => showToast(e.message, { error: true }));
  });

  els.outType.addEventListener("change", () => {
    updateQualityVisibility();
  });

  els.btnRender.addEventListener("click", () => renderPreview());
  els.btnExport.addEventListener("click", () => exportAndDownload());
  els.btnReset.addEventListener("click", () => resetAll());

  window.addEventListener("beforeunload", () => {
    cleanupWmImage();
    cleanupBaseImage();
  });
}

init();

