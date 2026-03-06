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
  previewImg: document.getElementById("previewImg"),
  srcMeta: document.getElementById("srcMeta"),
  outMeta: document.getElementById("outMeta"),
  outType: document.getElementById("outType"),
  qualityField: document.getElementById("qualityField"),
  quality: document.getElementById("quality"),
  qualityNum: document.getElementById("qualityNum"),
  qualityText: document.getElementById("qualityText"),
  btnRun: document.getElementById("btnRun"),
  btnDownload: document.getElementById("btnDownload"),
  btnReset: document.getElementById("btnReset"),
  toast: document.getElementById("toast"),
};

let state = {
  file: null,
  img: null,
  imgUrl: null,
  outBlob: null,
  outMime: "image/png",
  outFilename: "",
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

function setButtons({ canRun, canDownload } = {}) {
  els.btnRun.disabled = !canRun;
  els.btnDownload.disabled = !canDownload;
}

function cleanupImage() {
  if (state.imgUrl) revokeObjectUrl(state.imgUrl);
  state.imgUrl = null;
  state.img = null;
  els.previewImg.removeAttribute("src");
}

function cleanupOutput() {
  state.outBlob = null;
  state.outFilename = "";
  els.outMeta.textContent = "-";
  setButtons({ canRun: !!state.img, canDownload: false });
}

function resetAll() {
  hideToast();
  cleanupOutput();
  cleanupImage();
  state.file = null;
  els.fileInput.value = "";
  els.previewWrap.style.display = "none";
  els.srcMeta.textContent = "-";
  els.outType.value = "image/png";
  updateQualityVisibility();
  syncQualityUI(0.9);
  setButtons({ canRun: false, canDownload: false });
}

function syncQualityUI(v) {
  const q = clamp(Number(v) || 0.9, 0.1, 1);
  els.quality.value = String(q);
  els.qualityNum.value = String(q);
  els.qualityText.textContent = q.toFixed(2);
}

function updateQualityVisibility() {
  const mime = els.outType.value;
  const show = mime === "image/jpeg" || mime === "image/webp";
  els.qualityField.style.display = show ? "" : "none";
}

async function setFile(file) {
  hideToast();
  if (!file) return;
  if (!isImageFile(file)) {
    showToast("仅支持图片文件（JPG / PNG / WebP 等）。", { error: true });
    return;
  }

  cleanupOutput();
  cleanupImage();

  state.file = file;
  const { img, objectUrl } = await loadImageFromFile(file);
  state.img = img;
  state.imgUrl = objectUrl;
  els.previewImg.src = objectUrl;
  els.previewWrap.style.display = "";
  els.srcMeta.textContent = `${file.name} · ${img.naturalWidth}×${img.naturalHeight} · ${formatBytes(
    file.size,
  )}`;
  setButtons({ canRun: true, canDownload: false });
}

function drawToCanvas() {
  const img = state.img;
  if (!img) throw new Error("请先上传图片。");
  const w = img.naturalWidth || img.width;
  const h = img.naturalHeight || img.height;
  const canvas = document.createElement("canvas");
  canvas.width = Math.max(1, Math.floor(w));
  canvas.height = Math.max(1, Math.floor(h));
  const ctx = canvas.getContext("2d");
  if (!ctx) throw new Error("无法获取 Canvas 上下文。");
  ctx.imageSmoothingEnabled = true;
  ctx.imageSmoothingQuality = "high";
  ctx.drawImage(img, 0, 0);
  return canvas;
}

async function runConvert() {
  hideToast();
  if (!state.img || !state.file) {
    showToast("请先上传图片。", { error: true });
    return;
  }

  const mime = els.outType.value;
  state.outMime = mime;
  const q = clamp(Number(els.quality.value) || 0.9, 0.1, 1);
  try {
    const canvas = drawToCanvas();
    const blob = await canvasToBlob(canvas, {
      type: mime,
      quality: mime === "image/jpeg" || mime === "image/webp" ? q : undefined,
    });
    state.outBlob = blob;
    const ext = guessExtFromMime(mime);
    const base = safeBaseName(state.file.name);
    state.outFilename = `${base}.${ext}`;
    els.outMeta.textContent = `${state.outFilename} · ${canvas.width}×${canvas.height} · ${formatBytes(
      blob.size,
    )}`;
    setButtons({ canRun: true, canDownload: true });
  } catch (e) {
    cleanupOutput();
    showToast(e?.message || "转换失败，请重试。", { error: true });
  }
}

function download() {
  hideToast();
  if (!state.outBlob) {
    showToast("请先点击“开始转换”生成结果。", { error: true });
    return;
  }
  downloadBlob(state.outBlob, state.outFilename || `converted.${guessExtFromMime(state.outMime)}`);
}

function init() {
  updateQualityVisibility();
  syncQualityUI(els.quality.value);
  setButtons({ canRun: false, canDownload: false });

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

  els.outType.addEventListener("change", () => {
    updateQualityVisibility();
    cleanupOutput();
  });
  els.quality.addEventListener("input", () => {
    syncQualityUI(els.quality.value);
    cleanupOutput();
  });
  els.qualityNum.addEventListener("input", () => {
    syncQualityUI(els.qualityNum.value);
    cleanupOutput();
  });

  els.btnRun.addEventListener("click", () => runConvert());
  els.btnDownload.addEventListener("click", () => download());
  els.btnReset.addEventListener("click", () => resetAll());

  window.addEventListener("beforeunload", () => cleanupImage());
}

init();

