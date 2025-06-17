package org.example.dao;

import org.example.database.Conexao;
import org.example.model.Palestrante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PalestranteDAO {
    private Connection connection;

    public PalestranteDAO() {
        try {
            this.connection = new Conexao().createConnection();
        } catch (Exception e) {
            System.err.println("Erro ao obter conexão com o banco de dados para PalestranteDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void adicionarPalestrante(Palestrante palestrante) {
        String sql = "INSERT INTO palestrantes (nome, minicurriculo) VALUES (?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, palestrante.getNome());
            pst.setString(2, palestrante.getMinicurriculo());
            pst.executeUpdate();
            System.out.println("Palestrante '" + palestrante.getNome() + "' adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar palestrante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Palestrante> listarTodosPalestrantes() {
        List<Palestrante> palestrantes = new ArrayList<>();
        String sql = "SELECT id, nome, minicurriculo FROM palestrantes";

        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Palestrante palestrante = new Palestrante(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("minicurriculo")
                );
                palestrantes.add(palestrante);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar palestrantes: " + e.getMessage());
            e.printStackTrace();
        }
        return palestrantes;
    }

    public Palestrante buscarPalestrantePorId(int id) {
        Palestrante palestrante = null;
        String sql = "SELECT id, nome, minicurriculo FROM palestrantes WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    palestrante = new Palestrante(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("minicurriculo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar palestrante por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return palestrante;
    }

    public void atualizarPalestrante(Palestrante palestrante) {
        String sql = "UPDATE palestrantes SET nome = ?, minicurriculo = ? WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, palestrante.getNome());
            pst.setString(2, palestrante.getMinicurriculo());
            pst.setInt(3, palestrante.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Palestrante ID " + palestrante.getId() + " atualizado com sucesso!");
            } else {
                System.out.println("Nenhum palestrante encontrado com ID " + palestrante.getId() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar palestrante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletarPalestrante(int id) {
        String sql = "DELETE FROM palestrantes WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Palestrante ID " + id + " deletado com sucesso!");
            } else {
                System.out.println("Nenhum palestrante encontrado com ID " + id + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar palestrante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados para PalestranteDAO fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão para PalestranteDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

