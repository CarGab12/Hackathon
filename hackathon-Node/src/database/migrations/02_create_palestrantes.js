exports.up = function(knex) {
  return knex.schema.createTable('palestrantes', table => {
    table.increments('id').primary();
    table.string('nome').notNullable();
    table.text('minicurriculo').nullable();
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('palestrantes');
};
