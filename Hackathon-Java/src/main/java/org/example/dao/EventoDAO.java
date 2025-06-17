package org.example.dao;

import org.example.database.Conexao;
import org.example.model.Evento;
import org.example.model.Palestrante;
import org.example.model.Curso;

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

    public EventoDAO() {
        try {
            this.connection = new Conexao().createConnection();
        } catch (Exception e) {
            System.err.println("Erro ao obter conexão com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void adicionarEvento(Evento evento) {

        String sql = "INSERT INTO eventos (nomeEvento, descricao, data_hora, local, fotos, palestrante_id, curso_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setString(1, evento.getNomeEvento());
            past.setString(2, evento.getDescricao());
            past.setTimestamp(3, Timestamp.valueOf(evento.getDataHora()));
            past.setString(4, evento.getLocal());
            past.setString(5, evento.getFotos());
            if (evento.getPalestranteId() > 0) {
                past.setInt(6, evento.getPalestranteId());
            } else {
                past.setNull(6, java.sql.Types.INTEGER);
            }
            if (evento.getCursoId() > 0) {
                past.setInt(7, evento.getCursoId());
            } else {
                past.setNull(7, java.sql.Types.INTEGER);
            }
            past.executeUpdate();
            System.out.println("Evento '" + evento.getNomeEvento() + "' adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Evento> listarEventosPorCurso(Integer cursoId) {
        List<Evento> eventos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT e.id, e.nomeEvento, e.descricao, e.data_hora, e.local, e.fotos, e.palestrante_id, p.nome AS palestrante_nome, p.minicurriculo AS palestrante_minicurriculo, e.curso_id, c.nome AS curso_nome ");
        sql.append("FROM eventos e ");
        sql.append("LEFT JOIN palestrantes p ON e.palestrante_id = p.id ");
        sql.append("LEFT JOIN curso c ON e.curso_id = c.id ");

        if (cursoId != null && cursoId > 0) {
            sql.append("WHERE e.curso_id = ?");
        }

        try (PreparedStatement past = connection.prepareStatement(sql.toString())) {
            if (cursoId != null && cursoId > 0) {
                past.setInt(1, cursoId);
            }

            try (ResultSet rs = past.executeQuery()) {
                while (rs.next()) {
                    Timestamp dataHoraSql = rs.getTimestamp("data_hora");
                    LocalDateTime dataHoraJava = (dataHoraSql != null) ? dataHoraSql.toLocalDateTime() : null;

                    int palestranteId = rs.getInt("palestrante_id");
                    Palestrante palestrante = null;
                    if (!rs.wasNull()) {
                        palestrante = new Palestrante(
                                palestranteId,
                                rs.getString("palestrante_nome"),
                                rs.getString("palestrante_minicurriculo")
                        );
                    }

                    int idCurso = rs.getInt("curso_id");
                    Curso curso = null;
                    if (!rs.wasNull()) {
                        curso = new Curso(
                                idCurso,
                                rs.getString("curso_nome")
                        );
                    }

                    Evento evento = new Evento(
                            rs.getInt("id"),
                            rs.getString("nomeEvento"),
                            rs.getString("descricao"),
                            dataHoraJava,
                            rs.getString("local"),
                            rs.getString("fotos"),
                            palestrante,
                            curso
                    );
                    eventos.add(evento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos por curso: " + e.getMessage());
            e.printStackTrace();
        }
        return eventos;
    }



    public List<Evento> listarTodosEventos() {
        return listarEventosPorCurso(null);
    }

    public Evento buscarEventoPorId(int id) {
        Evento evento = null;
        String sql = "SELECT e.id, e.nomeEvento, e.descricao, e.data_hora, e.local, e.fotos, e.palestrante_id, p.nome AS palestrante_nome, p.minicurriculo AS palestrante_minicurriculo, e.curso_id, c.nome AS curso_nome " +
                "FROM eventos e LEFT JOIN palestrantes p ON e.palestrante_id = p.id " +
                "LEFT JOIN curso c ON e.curso_id = c.id WHERE e.id = ?";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setInt(1, id);
            try (ResultSet rs = past.executeQuery()) {
                if (rs.next()) {
                    Timestamp dataHoraSql = rs.getTimestamp("data_hora");
                    LocalDateTime dataHoraJava = (dataHoraSql != null) ? dataHoraSql.toLocalDateTime() : null;

                    int palestranteId = rs.getInt("palestrante_id");
                    Palestrante palestrante = null;
                    if (!rs.wasNull()) {
                        palestrante = new Palestrante(
                                palestranteId,
                                rs.getString("palestrante_nome"),
                                rs.getString("palestrante_minicurriculo")
                        );
                    }

                    int idCurso = rs.getInt("curso_id");
                    Curso curso = null;
                    if (!rs.wasNull()) {
                        curso = new Curso(
                                idCurso,
                                rs.getString("curso_nome")
                        );
                    }

                    evento = new Evento(
                            rs.getInt("id"),
                            rs.getString("nomeEvento"),
                            rs.getString("descricao"),
                            dataHoraJava,
                            rs.getString("local"),
                            rs.getString("fotos"),
                            palestrante,
                            curso
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
        String sql = "UPDATE eventos SET nomeEvento = ?, descricao = ?, data_hora = ?, local = ?, fotos = ?, palestrante_id = ?, curso_id = ? WHERE id = ?";

        try (PreparedStatement past = connection.prepareStatement(sql)) {
            past.setString(1, evento.getNomeEvento());
            past.setString(2, evento.getDescricao());
            past.setTimestamp(3, Timestamp.valueOf(evento.getDataHora()));
            past.setString(4, evento.getLocal());
            past.setString(5, evento.getFotos());
            if (evento.getPalestranteId() > 0) {
                past.setInt(6, evento.getPalestranteId());
            } else {
                past.setNull(6, java.sql.Types.INTEGER);
            }
            if (evento.getCursoId() > 0) {
                past.setInt(7, evento.getCursoId());
            } else {
                past.setNull(7, java.sql.Types.INTEGER);
            }
            past.setInt(8, evento.getId());
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
    }
}