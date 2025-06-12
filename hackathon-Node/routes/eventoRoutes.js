const express = require('express');
const router = express.Router();
const controller = require('../controllers/eventoController');

router.get('/:id', controller.getEventoById);
module.exports = router;
