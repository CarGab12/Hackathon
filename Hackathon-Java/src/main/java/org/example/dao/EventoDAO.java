package org.example.dao;

import org.example.database.Conexao;
import org.example.model.Evento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    private Connection connection;
    private Evento evento;

    public EventoDAO() {
        try {
            this.connection = new Conexao().createConnection();
        } catch (Exception e) {
            System.err.println("Erro ao obter conexão com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // CREATE
    public void adicionarEvento(Evento evento) {

        String sql = "INSERT INTO eventos (nomeEvento, descricao, data_hora, local, fotos) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setString(1, evento.getNomeEvento());
            past.setString(2, evento.getDescricao());

            past.setTimestamp(3, Timestamp.valueOf(evento.getDataHora()));
            past.setString(4, evento.getLocal());
            past.setString(5, evento.getFotos());
            past.executeUpdate();
            System.out.println("Evento '" + evento.getNomeEvento() + "' adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Evento> listarTodosEventos() {
        List<Evento> eventos = new ArrayList<>();

        String sql = "SELECT id, nomeEvento, descricao, data_hora, local, fotos FROM eventos";

        try (PreparedStatement past = connection.prepareStatement(sql);
             ResultSet rs = past.executeQuery()) {

            while (rs.next()) {
                Timestamp dataHoraSql = rs.getTimestamp("data_hora");
                LocalDateTime dataHoraJava = (dataHoraSql != null) ? dataHoraSql.toLocalDateTime() : null;

                Evento evento = new Evento(
                        rs.getInt("id"),
                        rs.getString("nomeEvento"),
                        rs.getString("descricao"),
                        dataHoraJava,
                        rs.getString("local"),
                        rs.getString("fotos")
                );
                eventos.add(evento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }

    public Evento buscarEventoPorId(int id) {
        Evento evento = null;

        String sql = "SELECT id, nomeEvento, descricao, data_hora, local, fotos FROM eventos WHERE id = ?";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setInt(1, id);
            try (ResultSet rs = past.executeQuery()) {
                if (rs.next()) {
                    Timestamp dataHoraSql = rs.getTimestamp("data_hora");
                    LocalDateTime dataHoraJava = (dataHoraSql != null) ? dataHoraSql.toLocalDateTime() : null;

                    evento = new Evento(
                            rs.getInt("id"),
                            rs.getString("nomeEvento"),
                            rs.getString("descricao"),
                            dataHoraJava, // Usar dataHoraJava
                            rs.getString("local"),
                            rs.getString("fotos")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar evento por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return evento;
    }

    public void atualizarEvento(Evento evento) {
        String sql = "UPDATE eventos SET nomeEvento = ?, descricao = ?, data_hora = ?, local = ?, fotos = ? WHERE id = ?";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setString(1, evento.getNomeEvento());
            past.setString(2, evento.getDescricao());
            past.setTimestamp(3, Timestamp.valueOf(evento.getDataHora()));
            past.setString(4, evento.getLocal());
            past.setString(5, evento.getFotos());
            past.setInt(6, evento.getId());
            int rowsAffected = past.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Evento ID " + evento.getId() + " atualizado com sucesso!");
            } else {
                System.out.println("Nenhum evento encontrado com ID " + evento.getId() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletarEvento(int id) {
        String sql = "DELETE FROM eventos WHERE id = ?";
        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setInt(1, id);
            int rowsAffected = past.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Evento ID " + id + " deletado com sucesso!");
            } else {
                System.out.println("Nenhum evento encontrado com ID " + id + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void create(Evento evento) {
        this.evento = evento;
    }
}