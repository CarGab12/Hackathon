const express = require('express');
const EventoController = require('../controllers/EventoController');
const UsuarioController = require('../controllers/UsuarioController');

const routes = express.Router();

routes.get('/eventos', EventoController.index);
routes.get('/eventos/:id', EventoController.show);


routes.get('/usuarios', UsuarioController.index);
routes.post('/usuarios', UsuarioController.create);

module.exports = routes;
