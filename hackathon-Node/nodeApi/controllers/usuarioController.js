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
  const { nome, cpf, email, telefone } = req.body;
  const inscricao = gerarNumeroAleatorio();
  const sql = 'INSERT INTO usuarios (inscricao, nome, cpf, email, telefone) VALUES (?, ?, ?, ?, ?)';
  db.query(sql, [inscricao, nome, cpf, email, telefone], (err, result) => {
    if (err) return res.status(500).json(err);
    res.status(201).json({ id: result.insertId, inscricao, nome, cpf, email, telefone });
  });
};