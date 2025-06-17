package org.example.gui;

import org.example.dao.EventoDAO;
import org.example.dao.PalestranteDAO;
import org.example.dao.CursoDAO;
import org.example.model.Evento;
import org.example.model.Palestrante;
import org.example.model.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EventosGUI extends JFrame {

    private EventoDAO eventoDAO;
    private PalestranteDAO palestranteDAO;
    private CursoDAO cursoDAO;

    private JTextField txtNomeEvento, txtDescricao, txtDataHora, txtLocal, txtFotos, txtId;
    private JComboBox<Palestrante> cmbPalestrante;
    private JComboBox<Curso> cmbCurso;
    private JComboBox<String> cmbFiltroCurso;
    private JButton btnAdicionar, btnAtualizar, btnDeletar, btnListar, btnSelecionarFoto, btnGerenciarPalestrantes, btnGerenciarCursos;

    private JTable tabelaEventos;
    private DefaultTableModel tableModel;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String selectedImagePath = null;

    private static final String FOTOS_BACKUP_DIR = "fotos_backup";

    public EventosGUI() {
        super("Gerenciamento de Eventos");
        this.eventoDAO = new EventoDAO();
        this.palestranteDAO = new PalestranteDAO();
        this.cursoDAO = new CursoDAO();
        initComponents();
        carregarTabelaEventos(null);
        carregarPalestrantesNoComboBox();
        carregarCursosNoComboBox();
        carregarFiltroCursos();

        File backupDir = new File(FOTOS_BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
            System.out.println("Diretório de backup de fotos criado: " + backupDir.getAbsolutePath());
        }
    }

    private void initComponents() {
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));
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

        cmbPalestrante = new JComboBox<>();
        inputPanel.add(new JLabel("Palestrante:"));
        inputPanel.add(cmbPalestrante);

        cmbCurso = new JComboBox<>();
        inputPanel.add(new JLabel("Curso:"));
        inputPanel.add(cmbCurso);

        add(inputPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnListar = new JButton("Recarregar Eventos");
        btnGerenciarPalestrantes = new JButton("Gerenciar Palestrantes");
        btnGerenciarCursos = new JButton("Gerenciar Cursos");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnDeletar);
        buttonPanel.add(btnListar);
        buttonPanel.add(btnGerenciarPalestrantes);
        buttonPanel.add(btnGerenciarCursos);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.add(new JLabel("Filtrar por Curso:"));
        cmbFiltroCurso = new JComboBox<>();
        cmbFiltroCurso.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(cmbFiltroCurso);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(filterPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        String[] columnNames = {"ID", "Nome Evento", "Descrição", "Data e Hora", "Local", "Fotos", "Palestrante", "Curso"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabelaEventos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEventos);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarEvento());
        btnAtualizar.addActionListener(e -> atualizarEvento());
        btnDeletar.addActionListener(e -> deletarEvento());
        btnListar.addActionListener(e -> carregarTabelaEventos(null));
        btnSelecionarFoto.addActionListener(e -> selecionarCaminhoFoto());
        btnGerenciarPalestrantes.addActionListener(e -> abrirGerenciadorPalestrantes());
        btnGerenciarCursos.addActionListener(e -> abrirGerenciadorCursos());

        cmbFiltroCurso.addActionListener(e -> filtrarEventosPorCurso());

        tabelaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaEventos.getSelectedRow() != -1) {
                int selectedRow = tabelaEventos.getSelectedRow();
                txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtNomeEvento.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtDescricao.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtDataHora.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtLocal.setText(tableModel.getValueAt(selectedRow, 4).toString());
                txtFotos.setText(tableModel.getValueAt(selectedRow, 5).toString());

                selectedImagePath = null;

                String palestranteNomeNaTabela = tableModel.getValueAt(selectedRow, 6).toString();
                if (palestranteNomeNaTabela != null && !palestranteNomeNaTabela.equals("N/A")) {
                    for (int i = 0; i < cmbPalestrante.getItemCount(); i++) {
                        Palestrante p = cmbPalestrante.getItemAt(i);
                        if (p != null && p.getNome().equals(palestranteNomeNaTabela)) {
                            cmbPalestrante.setSelectedItem(p);
                            break;
                        }
                    }
                } else {
                    cmbPalestrante.setSelectedIndex(-1);
                }

                String cursoNomeNaTabela = tableModel.getValueAt(selectedRow, 7).toString();
                if (cursoNomeNaTabela != null && !cursoNomeNaTabela.equals("N/A")) {
                    for (int i = 0; i < cmbCurso.getItemCount(); i++) {
                        Curso c = cmbCurso.getItemAt(i);
                        if (c != null && c.getNome().equals(cursoNomeNaTabela)) {
                            cmbCurso.setSelectedItem(c);
                            break;
                        }
                    }
                } else {
                    cmbCurso.setSelectedIndex(-1);
                }
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
            this.selectedImagePath = selectedFile.getAbsolutePath();
            txtFotos.setText(selectedFile.getName());
        }
    }

    private void carregarPalestrantesNoComboBox() {
        cmbPalestrante.removeAllItems();
        cmbPalestrante.addItem(null);
        List<Palestrante> palestrantes = palestranteDAO.listarTodosPalestrantes();
        for (Palestrante p : palestrantes) {
            cmbPalestrante.addItem(p);
        }
    }

    private void carregarCursosNoComboBox() {
        cmbCurso.removeAllItems();
        cmbCurso.addItem(null);
        List<Curso> cursos = cursoDAO.listarTodosCursos();
        for (Curso c : cursos) {
            cmbCurso.addItem(c);
        }
    }

    private void carregarFiltroCursos() {
        cmbFiltroCurso.removeAllItems();
        cmbFiltroCurso.addItem("Todos os Cursos");
        List<Curso> cursos = cursoDAO.listarTodosCursos();
        for (Curso c : cursos) {
            cmbFiltroCurso.addItem(c.getNome());
        }
    }

    private void abrirGerenciadorPalestrantes() {
        PalestrantesGUI palestrantesGUI = new PalestrantesGUI();
        palestrantesGUI.setVisible(true);
        palestrantesGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                carregarPalestrantesNoComboBox();
            }
        });
    }

    private void abrirGerenciadorCursos() {
        CursosGUI cursosGUI = new CursosGUI();
        cursosGUI.setVisible(true);
        cursosGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                carregarCursosNoComboBox();
                carregarFiltroCursos();
                carregarTabelaEventos(null);
            }
        });
    }

    private void adicionarEvento() {
        try {
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                Path sourcePath = Paths.get(selectedImagePath);
                String fileName = txtFotos.getText();
                Path destinationPath = Paths.get(FOTOS_BACKUP_DIR, fileName);

                try {
                    // Copia o arquivo. REPLACE_EXISTING substitui se já existir uma imagem com o mesmo nome.
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Foto copiada para backup: " + destinationPath.toAbsolutePath());
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(this, "Erro ao copiar a foto para backup: " + ioException.getMessage(), "Erro de Cópia", JOptionPane.ERROR_MESSAGE);
                    ioException.printStackTrace();
                }
            }

            LocalDateTime dataHora = LocalDateTime.parse(txtDataHora.getText(), FORMATTER);
            Palestrante selectedPalestrante = (Palestrante) cmbPalestrante.getSelectedItem();
            int palestranteId = (selectedPalestrante != null) ? selectedPalestrante.getId() : 0;

            Curso selectedCurso = (Curso) cmbCurso.getSelectedItem();
            int cursoId = (selectedCurso != null) ? selectedCurso.getId() : 0;

            Evento novoEvento = new Evento(
                    txtNomeEvento.getText(),
                    txtDescricao.getText(),
                    dataHora,
                    txtLocal.getText(),
                    txtFotos.getText(), // txtFotos já contém apenas o nome do arquivo
                    palestranteId,
                    cursoId
            );
            eventoDAO.adicionarEvento(novoEvento);
            JOptionPane.showMessageDialog(this, "Evento adicionado com sucesso!");
            limparCampos();
            carregarTabelaEventos(null);
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

            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                Path sourcePath = Paths.get(selectedImagePath);
                String fileName = txtFotos.getText();
                Path destinationPath = Paths.get(FOTOS_BACKUP_DIR, fileName);

                try {
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Foto atualizada e copiada para backup: " + destinationPath.toAbsolutePath());
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(this, "Erro ao copiar a foto para backup na atualização: " + ioException.getMessage(), "Erro de Cópia", JOptionPane.ERROR_MESSAGE);
                    ioException.printStackTrace();
                }
            }

            int id = Integer.parseInt(txtId.getText());
            LocalDateTime dataHora = LocalDateTime.parse(txtDataHora.getText(), FORMATTER);
            Palestrante selectedPalestrante = (Palestrante) cmbPalestrante.getSelectedItem();
            int palestranteId = (selectedPalestrante != null) ? selectedPalestrante.getId() : 0;

            Curso selectedCurso = (Curso) cmbCurso.getSelectedItem();
            int cursoId = (selectedCurso != null) ? selectedCurso.getId() : 0;

            Evento eventoAtualizado = new Evento(
                    id,
                    txtNomeEvento.getText(),
                    txtDescricao.getText(),
                    dataHora,
                    txtLocal.getText(),
                    txtFotos.getText(),
                    palestranteId,
                    cursoId
            );
            eventoDAO.atualizarEvento(eventoAtualizado);
            JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!");
            limparCampos();
            carregarTabelaEventos(null);
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
                carregarTabelaEventos(null);
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarTabelaEventos(Integer cursoId) {
        tableModel.setRowCount(0);
        List<Evento> eventos = eventoDAO.listarEventosPorCurso(cursoId);
        for (Evento evento : eventos) {
            String formattedDateTime = (evento.getDataHora() != null) ? evento.getDataHora().format(FORMATTER) : "N/A";
            String palestranteNome = (evento.getPalestrante() != null) ? evento.getPalestrante().getNome() : "N/A";
            String cursoNome = (evento.getCurso() != null) ? evento.getCurso().getNome() : "N/A";

            tableModel.addRow(new Object[]{
                    evento.getId(),
                    evento.getNomeEvento(),
                    evento.getDescricao(),
                    formattedDateTime,
                    evento.getLocal(),
                    evento.getFotos(),
                    palestranteNome,
                    cursoNome
            });
        }
    }

    private void filtrarEventosPorCurso() {
        String selectedFilter = (String) cmbFiltroCurso.getSelectedItem();
        if (selectedFilter != null && selectedFilter.equals("Todos os Cursos")) {
            carregarTabelaEventos(null);
        } else {
            List<Curso> cursos = cursoDAO.listarTodosCursos();
            Integer idCursoSelecionado = null;
            for (Curso c : cursos) {
                if (c.getNome().equals(selectedFilter)) {
                    idCursoSelecionado = c.getId();
                    break;
                }
            }
            carregarTabelaEventos(idCursoSelecionado);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNomeEvento.setText("");
        txtDescricao.setText("");
        txtDataHora.setText("");
        txtLocal.setText("");
        txtFotos.setText("");
        cmbPalestrante.setSelectedIndex(-1);
        cmbCurso.setSelectedIndex(-1);
        this.selectedImagePath = null;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Windows Look and Feel não suportado neste ambiente.");
            e.printStackTrace();
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Nimbus Look and Feel também não pode ser carregado.");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Nenhum Look and Feel moderno pôde ser carregado. Usando o padrão.", "Erro de Aparência", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível carregar o Look and Feel. Usando o padrão.", "Erro de Aparência", JOptionPane.WARNING_MESSAGE);
        }

        SwingUtilities.invokeLater(() -> new EventosGUI());
    }
}