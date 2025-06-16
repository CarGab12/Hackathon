exports.up = function(knex) {
  return knex.schema.createTable('eventos', table => {
    table.increments('id').primary();
    table.string('nomeEvento');
    table.string('descricao');
    table.string('local');
    table.datetime('data_hora');
    table.string('fotos');
    table.string('curso_nome');
    table.string('palestrante_nome');
    table.string('minicurriculo');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('eventos');
};
