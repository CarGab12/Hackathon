package org.example.gui;

import org.example.dao.EventoDAO;
import org.example.model.Evento;
import org.example.service.EventoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.io.File;

public class EventosGUI extends JFrame {

    private EventoDAO eventoDAO;
    private JTextField txtNomeEvento, txtDescricao, txtDataHora, txtLocal, txtFotos, txtId;
    private JButton btnAdicionar, btnAtualizar, btnDeletar, btnListar, btnSelecionarFoto;
    private JTable tabelaEventos;
    private DefaultTableModel tableModel;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public EventosGUI() {
        super("Gerenciamento de Eventos - Hackathon");
        this.eventoDAO = new EventoDAO();
        initComponents();
        carregarTabelaEventos();
    }

    private void initComponents() {
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Evento"));

        txtId = new JTextField();
        txtId.setEditable(false);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(txtId);

        txtNomeEvento = new JTextField();
        inputPanel.add(new JLabel("Nome do Evento:"));
        inputPanel.add(txtNomeEvento);

        txtDescricao = new JTextField();
        inputPanel.add(new JLabel("Descrição:"));
        inputPanel.add(txtDescricao);

        txtDataHora = new JTextField();
        inputPanel.add(new JLabel("Data e Hora (DD/MM/AAAA HH:MM:SS):"));
        inputPanel.add(txtDataHora);

        txtLocal = new JTextField();
        inputPanel.add(new JLabel("Local:"));
        inputPanel.add(txtLocal);

        JPanel fotoPanel = new JPanel(new BorderLayout(5, 0));
        txtFotos = new JTextField();
        btnSelecionarFoto = new JButton("...");
        fotoPanel.add(txtFotos, BorderLayout.CENTER);
        fotoPanel.add(btnSelecionarFoto, BorderLayout.EAST);

        inputPanel.add(new JLabel("Caminho da Foto:"));
        inputPanel.add(fotoPanel);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnListar = new JButton("Recarregar Lista");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnDeletar);
        buttonPanel.add(btnListar);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nome Evento", "Descrição", "Data e Hora", "Local", "Fotos"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabelaEventos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEventos);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarEvento());
        btnAtualizar.addActionListener(e -> atualizarEvento());
        btnDeletar.addActionListener(e -> deletarEvento());
        btnListar.addActionListener(e -> carregarTabelaEventos());
        btnSelecionarFoto.addActionListener(e -> selecionarCaminhoFoto());

        tabelaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaEventos.getSelectedRow() != -1) {
                int selectedRow = tabelaEventos.getSelectedRow();
                txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtNomeEvento.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtDescricao.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtDataHora.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtLocal.setText(tableModel.getValueAt(selectedRow, 4).toString());
                txtFotos.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });

        setVisible(true);
    }

    private void selecionarCaminhoFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione a Foto do Evento");

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtFotos.setText(selectedFile.getAbsolutePath());
        }
    }


    private void adicionarEvento() {
        try {
            LocalDateTime dataHora = LocalDateTime.parse(txtDataHora.getText(), FORMATTER);
            Evento novoEvento = new Evento(
                    txtNomeEvento.getText(),
                    txtDescricao.getText(),
                    dataHora,
                    txtLocal.getText(),
                    txtFotos.getText()
            );
            eventoDAO.adicionarEvento(novoEvento);
            JOptionPane.showMessageDialog(this, "Evento adicionado com sucesso!");
            limparCampos();
            carregarTabelaEventos();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de Data e Hora inválido. Use DD/MM/AAAA HH:MM:SS", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarEvento() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            LocalDateTime dataHora = LocalDateTime.parse(txtDataHora.getText(), FORMATTER);

            Evento eventoAtualizado = new Evento(
                    id,
                    txtNomeEvento.getText(),
                    txtDescricao.getText(),
                    dataHora,
                    txtLocal.getText(),
                    txtFotos.getText()
            );
            eventoDAO.atualizarEvento(eventoAtualizado);
            JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!");
            limparCampos();
            carregarTabelaEventos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de Data e Hora inválido. Use DD/MM/AAAA HH:MM:SS", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarEvento() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o evento ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eventoDAO.deletarEvento(id);
                JOptionPane.showMessageDialog(this, "Evento deletado com sucesso!");
                limparCampos();
                carregarTabelaEventos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarTabelaEventos() {
        tableModel.setRowCount(0);
        List<Evento> eventos = eventoDAO.listarTodosEventos();
        for (Evento evento : eventos) {
            String formattedDateTime = (evento.getDataHora() != null) ? evento.getDataHora().format(FORMATTER) : "N/A";
            tableModel.addRow(new Object[]{
                    evento.getId(),
                    evento.getNomeEvento(),
                    evento.getDescricao(),
                    formattedDateTime,
                    evento.getLocal(),
                    evento.getFotos()
            });
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNomeEvento.setText("");
        txtDescricao.setText("");
        txtDataHora.setText("");
        txtLocal.setText("");
        txtFotos.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventosGUI());
    }
}