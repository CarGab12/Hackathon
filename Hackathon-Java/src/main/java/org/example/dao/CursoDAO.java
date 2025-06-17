package org.example.dao;

import org.example.database.Conexao;
import org.example.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    private Connection connection;

    public CursoDAO() {
        try {
            this.connection = new Conexao().createConnection();
        } catch (Exception e) {
            System.err.println("Erro ao obter conexão com o banco de dados para CursoDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void adicionarCurso(Curso curso) {
        String sql = "INSERT INTO curso (nome) VALUES (?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, curso.getNome());
            pst.executeUpdate();
            System.out.println("Curso '" + curso.getNome() + "' adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Curso> listarTodosCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT id, nome FROM curso";
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
                cursos.add(curso);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cursos: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    public Curso buscarCursoPorId(int id) {
        Curso curso = null;
        String sql = "SELECT id, nome FROM curso WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    curso = new Curso(
                            rs.getInt("id"),
                            rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar curso por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return curso;
    }

    public void atualizarCurso(Curso curso) {
        String sql = "UPDATE curso SET nome = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, curso.getNome());
            pst.setInt(2, curso.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Curso ID " + curso.getId() + " atualizado com sucesso!");
            } else {
                System.out.println("Nenhum curso encontrado com ID " + curso.getId() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletarCurso(int id) {
        String sql = "DELETE FROM curso WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Curso ID " + id + " deletado com sucesso!");
            } else {
                System.out.println("Nenhum curso encontrado com ID " + id + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados para CursoDAO fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão para CursoDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
