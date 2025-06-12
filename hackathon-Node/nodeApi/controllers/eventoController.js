const db = require('../db');

exports.getEventoById = (req, res) => {
  const { id } = req.params;
  db.query('SELECT * FROM eventos WHERE id = ?', [id], (err, results) => {
    if (err) return res.status(500).json(err);
    if (results.length === 0) return res.status(404).json({ mensagem: 'Evento não encontrado' });
    res.json(results[0]);
  });
};


