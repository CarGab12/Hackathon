const connection = require('../database/connection');

async function index(req, res) {
  const eventos = await connection('eventos')
    .leftJoin('curso', 'curso.id', 'eventos.curso_id')
    .leftJoin('palestrantes', 'palestrantes.id', 'eventos.palestrantes_id')
    .select(
      'eventos.*',
      'curso.nome as curso_nome',
      'palestrantes.nome as palestrante_nome',
      'palestrantes.minicurriculo'
    );
  res.json(eventos);
}

async function show(req, res) {
  const { id } = req.params;
  const evento = await connection('eventos')
    .leftJoin('curso', 'curso.id', 'eventos.curso_id')
    .leftJoin('palestrantes', 'palestrantes.id', 'eventos.palestrantes_id')
    .select(
      'eventos.*',
      'curso.nome as curso_nome',
      'palestrantes.nome as palestrante_nome',
      'palestrantes.minicurriculo'
    )
    .where('eventos.id', id)
    .first();

  if (!evento) {
    return res.status(404).json({ erro: 'Evento não encontrado' });
  }

  res.json(evento);
}

module.exports = { index, show };
