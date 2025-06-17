const knex = require('knex');
const config = require('../../knexfile');

const connection = knex(config.development);

connection.raw('SELECT 1')
  .then(() => {
    console.log('Conexão com o banco de dados estabelecida com sucesso.');
  })
  .catch((err) => {
    console.error('Erro ao conectar com o banco de dados:', err.message);
  });

module.exports = connection;
