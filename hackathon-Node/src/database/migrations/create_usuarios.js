exports.up = function(knex) {
  return knex.schema.createTable('usuarios', table => {
    table.increments('id').primary();
    table.string('nome');
    table.string('cpf');
    table.string('email');
    table.string('telefone');
    table.string('inscricao');
    table.integer('evento_id').unsigned().notNullable();
    table.foreign('evento_id').references('id').inTable('eventos');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('usuarios');
};
