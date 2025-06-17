exports.up = function(knex) {
  return knex.schema.createTable('eventos', table => {
    table.increments('id').primary();
    table.string('nomeEvento').notNullable();
    table.text('descricao').notNullable();
    table.string('local').notNullable();
    table.datetime('data_hora').notNullable();
    table.string('fotos').nullable();
    table.integer('curso_id').unsigned().references('id').inTable('curso').onDelete('SET NULL');
    table.integer('palestrantes_id').unsigned().references('id').inTable('palestrantes').onDelete('SET NULL');
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('eventos');
};
