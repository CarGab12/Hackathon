package org.example.gui;

import org.example.dao.PalestranteDAO;
import org.example.model.Palestrante;
import org.example.service.PalestranteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PalestrantesGUI extends JFrame {

    private PalestranteService palestranteService;
    private JTextField txtId, txtNome, txtMinicurriculo;
    private JButton btnAdicionar, btnAtualizar, btnDeletar, btnListar;
    private JTable tabelaPalestrantes;
    private DefaultTableModel tableModel;

    public PalestrantesGUI() {
        super("Gerenciamento de Palestrantes");
        this.palestranteService = new PalestranteService();
        initComponents();
        carregarTabelaPalestrantes();
    }

    private void initComponents() {
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Palestrante"));

        txtId = new JTextField();
        txtId.setEditable(false);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(txtId);

        txtNome = new JTextField();
        inputPanel.add(new JLabel("Nome:"));
        inputPanel.add(txtNome);

        txtMinicurriculo = new JTextField();
        inputPanel.add(new JLabel("Minicurrículo:"));
        inputPanel.add(txtMinicurriculo);

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

        String[] columnNames = {"ID", "Nome", "Minicurrículo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabelaPalestrantes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaPalestrantes);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarPalestrante());
        btnAtualizar.addActionListener(e -> atualizarPalestrante());
        btnDeletar.addActionListener(e -> deletarPalestrante());
        btnListar.addActionListener(e -> carregarTabelaPalestrantes());

        tabelaPalestrantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaPalestrantes.getSelectedRow() != -1) {
                int selectedRow = tabelaPalestrantes.getSelectedRow();
                txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtNome.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtMinicurriculo.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        setVisible(true);
    }

    private void adicionarPalestrante() {
        try {
            Palestrante novoPalestrante = new Palestrante(
                    txtNome.getText(),
                    txtMinicurriculo.getText()
            );
            palestranteService.adicionarPalestrante(novoPalestrante);
            JOptionPane.showMessageDialog(this, "Palestrante adicionado com sucesso!");
            limparCampos();
            carregarTabelaPalestrantes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar palestrante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarPalestrante() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            Palestrante palestranteAtualizado = new Palestrante(
                    id,
                    txtNome.getText(),
                    txtMinicurriculo.getText()
            );
            palestranteService.atualizarPalestrante(palestranteAtualizado);
            JOptionPane.showMessageDialog(this, "Palestrante atualizado com sucesso!");
            limparCampos();
            carregarTabelaPalestrantes();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar palestrante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarPalestrante() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um palestrante na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o palestrante ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                palestranteService.deletarPalestrante(id);
                JOptionPane.showMessageDialog(this, "Palestrante deletado com sucesso!");
                limparCampos();
                carregarTabelaPalestrantes();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar palestrante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarTabelaPalestrantes() {
        tableModel.setRowCount(0);
        List<Palestrante> palestrantes = palestranteService.listarTodosPalestrantes();
        for (Palestrante palestrante : palestrantes) {
            tableModel.addRow(new Object[]{
                    palestrante.getId(),
                    palestrante.getNome(),
                    palestrante.getMinicurriculo()
            });
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtMinicurriculo.setText("");
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