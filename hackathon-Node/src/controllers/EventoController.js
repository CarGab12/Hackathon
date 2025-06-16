const connection = require('../database/connection');

module.exports = {
  async index(req, res) {
    const eventos = await connection('eventos').select('*');
    res.json(eventos);
  },

  async show(req, res) {
    const { id } = req.params;
    const evento = await connection('eventos').where('id', id).first();

    if (!evento) {
      return res.status(404).json({ erro: 'Evento não encontrado' });
    }

    res.json(evento);
  }
};
