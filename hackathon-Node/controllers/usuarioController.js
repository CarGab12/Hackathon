const db = require('../db');

function gerarNumeroAleatorio() {
  return Math.floor(Math.random() * 900000 + 100000).toString();
}
exports.getUsuarios = (req, res) => {
  db.query('SELECT * FROM usuarios', (err, results) => {
    if (err) return res.status(500).json(err);
    res.json(results);
  });
};
exports.createUsuario = (req, res) => {
  const { nome, cpf, email, telefone, evento_id } = req.body;

  const verificarSQL = 'SELECT * FROM usuarios WHERE cpf = ? AND evento_id = ?';
  db.query(verificarSQL, [cpf, evento_id], (err, results) => {
    if (err) return res.status(500).json({ erro: 'Erro ao verificar CPF', detalhes: err });

    if (results.length > 0) {
      return res.status(400).json({ mensagem: 'Este CPF já está inscrito nesse evento.' });
    }

    const inscricao = gerarNumeroAleatorio();
    const insertSQL = 'INSERT INTO usuarios (inscricao, nome, cpf, email, telefone, evento_id) VALUES (?, ?, ?, ?, ?, ?)';
    db.query(insertSQL, [inscricao, nome, cpf, email, telefone, evento_id], (err, result) => {
      if (err) return res.status(500).json({ erro: 'Erro ao cadastrar', detalhes: err });

      res.status(201).json({ id: result.insertId, inscricao, nome, cpf, email, telefone, evento_id });
    });
  });
};
