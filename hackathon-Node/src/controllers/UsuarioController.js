const connection = require('../database/connection');
const crypto = require('crypto');

module.exports = {
  async store(req, res) {
    const { nome, cpf, email, telefone, evento_id } = req.body;

    const usuarioExistente = await connection('usuarios')
      .where({ cpf, evento_id })
      .first();

    if (usuarioExistente) {
      return res.status(400).json({
        mensagem: 'Este CPF já está inscrito em um evento neste horário'
      });
    }

    const inscricao = crypto.randomBytes(4).toString('hex');

    await connection('usuarios').insert({
      nome,
      cpf,
      email,
      telefone,
      evento_id,
      inscricao
    });

    res.status(201).json({ inscricao });
  },

  async index(req, res) {
    const usuarios = await connection('usuarios').select('*');
    res.json(usuarios);
  }
};
