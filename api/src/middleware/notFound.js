// file: src/middleware/notFound.js

const { HttpError } = require('../utils/httpError');

function notFound(req, res, next) {
  next(new HttpError(404, 'NOT_FOUND', `Cannot ${req.method} ${req.path}`));
}

module.exports = { notFound };
