const knex = require('knex');
const config = require('../../knexfile');

const connection = knex(config.development);
connection.raw('SELECT 1')
  .then(() => {
    console.log('Conexão com o banco de dados estabelecida com sucesso!');
  })
  .catch((err) => {
    console.error('Erro ao conectar ao banco de dados:', err);
  });
module.exports = connection;
