// file: src/modules/user/user.controller.js

const userService = require('./user.service');
const { HttpError } = require('../../utils/httpError');

async function create(req, res) {
  const body = req.body || {};
  const name = body.name != null ? String(body.name).trim() : '';
  if (!name) {
    throw new HttpError(400, 'VALIDATION_ERROR', 'name is required');
  }
  const data = userService.create({ name, ...body });
  res.status(201).json({ data });
}

async function getById(req, res) {
  const id = req.params.id;
  if (!id) {
    throw new HttpError(400, 'VALIDATION_ERROR', 'id is required');
  }
  const data = userService.getById(id);
  res.json({ data });
}

async function update(req, res) {
  const id = req.params.id;
  const patch = req.body || {};
  if (!id) {
    throw new HttpError(400, 'VALIDATION_ERROR', 'id is required');
  }
  const data = userService.update(id, patch);
  res.json({ data });
}

async function remove(req, res) {
  const id = req.params.id;
  if (!id) {
    throw new HttpError(400, 'VALIDATION_ERROR', 'id is required');
  }
  const data = userService.delete(id);
  res.json({ data });
}

async function list(req, res) {
  const page = req.query.page;
  const pageSize = req.query.pageSize;
  const result = userService.list({ page, pageSize });
  res.json({
    data: result.items,
    meta: {
      page: result.page,
      pageSize: result.pageSize,
      total: result.total,
    },
  });
}

module.exports = {
  create,
  getById,
  update,
  delete: remove,
  list,
};
