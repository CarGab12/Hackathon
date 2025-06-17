package org.example;

import org.example.database.Conexao;
import org.example.gui.EventosGUI;
import org.example.model.Evento;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EventosGUI();
        });
    }
}