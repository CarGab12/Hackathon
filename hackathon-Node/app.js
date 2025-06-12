const express = require('express');
const app = express();
require('dotenv').config();
const db = require('./db');

const eventoRoutes = require('./routes/eventoRoutes');
const usuarioRoutes = require('./routes/usuarioRoutes');

app.use(express.json());
app.use('/api/eventos', eventoRoutes);
app.use('/api/usuarios', usuarioRoutes);

app.listen(3000, () => {
  console.log('Servidor rodando em http://localhost:3000');
});