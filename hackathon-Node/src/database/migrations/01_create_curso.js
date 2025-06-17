exports.up = function(knex) {
  return knex.schema.createTable('curso', table => {
    table.increments('id').primary();
    table.string('nome').notNullable();
  });
};

exports.down = function(knex) {
  return knex.schema.dropTable('curso');
};
