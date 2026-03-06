// file: src/modules/user/user.router.js

const express = require('express');
const { asyncHandler } = require('../../utils/asyncHandler');
const userController = require('./user.controller');

const router = express.Router();

router.post('/', asyncHandler(userController.create));
router.get('/', asyncHandler(userController.list));
router.get('/:id', asyncHandler(userController.getById));
router.patch('/:id', asyncHandler(userController.update));
router.delete('/:id', asyncHandler(userController.delete));

module.exports = router;
