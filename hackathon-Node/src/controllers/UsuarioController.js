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
        mensagem: 'Este CPF já está inscrito nesse evento ou em um evento neste horário'
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
  },


  async certificado(req, res) {
    const { id } = req.params;

    try {
      const usuario = await connection('usuarios')
        .join('eventos', 'eventos.id', 'usuarios.evento_id')
        .where('usuarios.inscricao', id)
        .select(
          'usuarios.nome as aluno',
          'eventos.nomeEvento as evento',
          'eventos.local',
          'eventos.data_hora'
        )
        .first();

      if (!usuario) {
        return res.status(404).json({ erro: 'Inscrição não encontrada' });
      }

      res.json(usuario);
    } catch (error) {
      console.error('Erro ao buscar inscrição:', error);
      res.status(500).json({ erro: 'Erro interno no servidor' });
    }
  }
};
