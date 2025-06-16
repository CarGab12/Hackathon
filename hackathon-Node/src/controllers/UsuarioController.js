const connection = require('../database/connection');

function gerarNumeroAleatorio() {
  return Math.floor(Math.random() * 900000 + 100000).toString();
}

module.exports = {
  async index(req, res) {
    try {
      const usuarios = await connection('usuarios').select('*');
      res.json(usuarios);
    } catch (err) {
      res.status(500).json({ erro: 'Erro ao listar usuarios', detalhes: err });
    }
  },

  async create(req, res) {
    const { nome, cpf, email, telefone, evento_id } = req.body;
    if (!nome || !cpf || !evento_id) {
      return res.status(400).json({ erro: 'Campos obrigatórios ausentes' });
    }

    try {
      const usuarioExistente = await connection('usuarios')
        .where({ cpf, evento_id })
        .first();

      if (usuarioExistente) {
        return res.status(400).json({ mensagem: 'Este CPF já está inscrito neste evento.' });
      }

      const inscricao = gerarNumeroAleatorio();

      const [id] = await connection('usuarios').insert({
        nome,
        cpf,
        email,
        telefone,
        evento_id,
        inscricao
      });

      return res.status(201).json({ id, nome, cpf, email, telefone, evento_id, inscricao });
    } catch (err) {
      return res.status(500).json({ erro: 'Erro ao cadastrar', detalhes: err });
    }
  }
};
