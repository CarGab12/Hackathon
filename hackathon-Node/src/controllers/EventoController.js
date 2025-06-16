const connection = require('../database/connection');

module.exports = {
  async index(req, res) {
    const eventos = await connection('eventos').select('*');
    return res.json(eventos);
  },

  async show(req, res) {
    const { id } = req.params;
    const evento = await connection('eventos').where('id', id).first();
    if (!evento) return res.status(404).json({ mensagem: 'Evento não encontrado' });
    return res.json(evento);
  }
};
