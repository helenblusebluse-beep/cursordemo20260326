// file: src/routes/index.js

const express = require('express');
const userRouter = require('../modules/user/user.router');

const router = express.Router();

router.use('/users', userRouter);

module.exports = router;
