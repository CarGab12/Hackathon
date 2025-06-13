exports.up = function(knex) {
  return knex.schema.createTable('eventos', table => {
    table.increments('id').primary();
    table.string('nomeEvento').notNullable();
    table.text('descricao');
    table.dateTime('data_hora');
    table.string('local');
    table.string('fotos');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('eventos');
};
