// file: src/modules/user/user.service.js

const crypto = require('crypto');
const { HttpError } = require('../../utils/httpError');

const store = new Map();

function create(payload) {
  const id = crypto.randomUUID();
  const now = new Date().toISOString();
  const item = {
    id,
    ...payload,
    createdAt: now,
    updatedAt: now,
  };
  store.set(id, item);
  return item;
}

function getById(id) {
  const item = store.get(id);
  if (!item) {
    throw new HttpError(404, 'NOT_FOUND', 'User not found');
  }
  return item;
}

function update(id, patch) {
  const item = store.get(id);
  if (!item) {
    throw new HttpError(404, 'NOT_FOUND', 'User not found');
  }
  const updated = {
    ...item,
    ...patch,
    id: item.id,
    createdAt: item.createdAt,
    updatedAt: new Date().toISOString(),
  };
  store.set(id, updated);
  return updated;
}

function remove(id) {
  if (!store.has(id)) {
    throw new HttpError(404, 'NOT_FOUND', 'User not found');
  }
  store.delete(id);
  return { id };
}

function list({ page = 1, pageSize = 10 } = {}) {
  const arr = Array.from(store.values());
  const total = arr.length;
  const p = Math.max(1, parseInt(page, 10) || 1);
  const ps = Math.min(100, Math.max(1, parseInt(pageSize, 10) || 10));
  const start = (p - 1) * ps;
  const items = arr.slice(start, start + ps);
  return {
    items,
    page: p,
    pageSize: ps,
    total,
  };
}

module.exports = {
  create,
  getById,
  update,
  delete: remove,
  list,
};
