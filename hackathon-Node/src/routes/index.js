const express = require('express');
const EventoController = require('../controllers/EventoController');
const UsuarioController = require('../controllers/UsuarioController');

const routes = express.Router();

routes.get('/eventos', EventoController.index);
routes.get('/eventos/:id', EventoController.show);
routes.post('/usuarios', UsuarioController.store);
routes.get('/usuarios', UsuarioController.index);

module.exports = routes;
