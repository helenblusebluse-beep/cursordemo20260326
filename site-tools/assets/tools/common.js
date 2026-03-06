export function formatBytes(bytes) {
  const n = Number(bytes);
  if (!Number.isFinite(n) || n <= 0) return "0 B";
  const units = ["B", "KB", "MB", "GB"];
  const i = Math.min(units.length - 1, Math.floor(Math.log(n) / Math.log(1024)));
  const value = n / 1024 ** i;
  const fixed = value >= 100 || i === 0 ? 0 : value >= 10 ? 1 : 2;
  return `${value.toFixed(fixed)} ${units[i]}`;
}

export function clamp(v, min, max) {
  return Math.min(max, Math.max(min, v));
}

export function createObjectUrl(fileOrBlob) {
  return URL.createObjectURL(fileOrBlob);
}

export function revokeObjectUrl(url) {
  try {
    URL.revokeObjectURL(url);
  } catch {
    // ignore
  }
}

export function isImageFile(file) {
  return !!file && typeof file.type === "string" && file.type.startsWith("image/");
}

export async function loadImageFromFile(file) {
  if (!isImageFile(file)) {
    throw new Error("请选择图片文件（JPG / PNG / WebP 等）。");
  }
  const url = createObjectUrl(file);
  try {
    const img = await loadImageFromUrl(url);
    return { img, objectUrl: url };
  } catch (e) {
    revokeObjectUrl(url);
    throw e;
  }
}

export function loadImageFromUrl(url) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => resolve(img);
    img.onerror = () => reject(new Error("图片加载失败，请换一张图片重试。"));
    img.src = url;
  });
}

export function pickFileFromInput(inputEl) {
  const file = inputEl?.files?.[0];
  return file || null;
}

export function bindDropzone(dropzoneEl, { onFile } = {}) {
  if (!dropzoneEl) return () => {};
  const stop = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const onDragEnter = (e) => {
    stop(e);
    dropzoneEl.classList.add("is-dragover");
  };
  const onDragOver = (e) => {
    stop(e);
    dropzoneEl.classList.add("is-dragover");
  };
  const onDragLeave = (e) => {
    stop(e);
    dropzoneEl.classList.remove("is-dragover");
  };
  const onDrop = (e) => {
    stop(e);
    dropzoneEl.classList.remove("is-dragover");
    const file = e.dataTransfer?.files?.[0] || null;
    if (file && typeof onFile === "function") onFile(file);
  };

  dropzoneEl.addEventListener("dragenter", onDragEnter);
  dropzoneEl.addEventListener("dragover", onDragOver);
  dropzoneEl.addEventListener("dragleave", onDragLeave);
  dropzoneEl.addEventListener("drop", onDrop);

  return () => {
    dropzoneEl.removeEventListener("dragenter", onDragEnter);
    dropzoneEl.removeEventListener("dragover", onDragOver);
    dropzoneEl.removeEventListener("dragleave", onDragLeave);
    dropzoneEl.removeEventListener("drop", onDrop);
  };
}

export async function canvasToBlob(canvas, { type, quality } = {}) {
  const mime = type || "image/png";
  const q = typeof quality === "number" ? quality : undefined;
  return await new Promise((resolve, reject) => {
    canvas.toBlob(
      (blob) => {
        if (!blob) reject(new Error("导出失败（浏览器未返回 Blob）。"));
        else resolve(blob);
      },
      mime,
      q,
    );
  });
}

export function downloadBlob(blob, filename) {
  const url = createObjectUrl(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename || "download";
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => revokeObjectUrl(url), 2000);
}

export function guessExtFromMime(mime) {
  if (!mime) return "png";
  const m = String(mime).toLowerCase();
  if (m.includes("jpeg") || m.includes("jpg")) return "jpg";
  if (m.includes("png")) return "png";
  if (m.includes("webp")) return "webp";
  return "png";
}

export function safeBaseName(name) {
  const n = (name || "image").replace(/\.[^/.]+$/, "");
  return n.replace(/[\\/:*?"<>|]+/g, "_").trim() || "image";
}

