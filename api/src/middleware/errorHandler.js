// file: src/middleware/errorHandler.js

const { HttpError } = require('../utils/httpError');

function errorHandler(err, req, res, next) {
  const statusCode = err instanceof HttpError ? err.statusCode : 500;
  const code = err instanceof HttpError ? err.code : 'INTERNAL_ERROR';
  const message = err.message || 'Internal Server Error';

  res.status(statusCode).json({
    error: {
      code,
      message,
      requestId: req.id || undefined,
    },
  });
}

module.exports = { errorHandler };
