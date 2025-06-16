exports.up = function(knex) {
  return knex.schema.createTable('usuarios', table => {
    table.increments('id').primary();
    table.string('inscricao').notNullable().unique();
    table.string('nome').notNullable();
    table.string('cpf').notNullable();
    table.string('email');
    table.string('telefone');
    table.integer('evento_id').unsigned();
    table.foreign('evento_id').references('id').inTable('eventos').onDelete('CASCADE');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('usuarios');
};
