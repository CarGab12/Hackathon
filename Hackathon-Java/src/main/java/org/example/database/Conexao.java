package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/hackathon?useTimezone=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public Connection createConnection() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC carregado com sucesso.");
            Connection connection = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC não encontrado. Adicione o JAR do driver ao classpath.");
            throw new Exception("Driver JDBC não encontrado. Adicione o JAR do driver ao classpath.", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw new Exception("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}
