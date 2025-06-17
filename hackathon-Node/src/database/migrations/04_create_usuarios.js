exports.up = function(knex) {
  return knex.schema.createTable('usuarios', table => {
    table.increments('id').primary();
    table.string('nome').notNullable();
    table.string('cpf').notNullable();
    table.string('email').notNullable();
    table.string('telefone').notNullable();
    table.string('inscricao').notNullable();
    table.integer('evento_id').unsigned().notNullable();
    table.foreign('evento_id').references('id').inTable('eventos').onDelete('CASCADE');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('usuarios');
};
