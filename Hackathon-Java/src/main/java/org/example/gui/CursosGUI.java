package org.example.gui;

import org.example.dao.CursoDAO;
import org.example.model.Curso;
import org.example.service.CursoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CursosGUI extends JFrame {

    private CursoService cursoService;
    private JTextField txtId, txtNome;
    private JButton btnAdicionar, btnAtualizar, btnDeletar, btnListar;
    private JTable tabelaCursos;
    private DefaultTableModel tableModel;

    public CursosGUI() {
        super("Gerenciamento de Cursos");
        this.cursoService = new CursoService();
        initComponents();
        carregarTabelaCursos();
    }

    private void initComponents() {
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Curso"));

        txtId = new JTextField();
        txtId.setEditable(false);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(txtId);

        txtNome = new JTextField();
        inputPanel.add(new JLabel("Nome do Curso:"));
        inputPanel.add(txtNome);

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

        String[] columnNames = {"ID", "Nome do Curso"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabelaCursos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCursos);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarCurso());
        btnAtualizar.addActionListener(e -> atualizarCurso());
        btnDeletar.addActionListener(e -> deletarCurso());
        btnListar.addActionListener(e -> carregarTabelaCursos());

        tabelaCursos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaCursos.getSelectedRow() != -1) {
                int selectedRow = tabelaCursos.getSelectedRow();
                txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtNome.setText(tableModel.getValueAt(selectedRow, 1).toString());
            }
        });

        setVisible(true);
    }

    private void adicionarCurso() {
        try {
            Curso novoCurso = new Curso(txtNome.getText());
            cursoService.adicionarCurso(novoCurso);
            JOptionPane.showMessageDialog(this, "Curso adicionado com sucesso!");
            limparCampos();
            carregarTabelaCursos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar curso: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarCurso() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um curso na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            Curso cursoAtualizado = new Curso(id, txtNome.getText());
            cursoService.atualizarCurso(cursoAtualizado);
            JOptionPane.showMessageDialog(this, "Curso atualizado com sucesso!");
            limparCampos();
            carregarTabelaCursos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar curso: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarCurso() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um curso na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o curso ID " + id + "? (Eventos associados terão seu curso_id definido como NULL)", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cursoService.deletarCurso(id);
                JOptionPane.showMessageDialog(this, "Curso deletado com sucesso!");
                limparCampos();
                carregarTabelaCursos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar curso: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarTabelaCursos() {
        tableModel.setRowCount(0);
        List<Curso> cursos = cursoService.listarTodosCursos();
        for (Curso curso : cursos) {
            tableModel.addRow(new Object[]{
                    curso.getId(),
                    curso.getNome()
            });
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
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