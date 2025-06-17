package org.example.service;

import org.example.model.Palestrante;
import org.example.dao.PalestranteDAO;

import java.util.List;

public class PalestranteService {
    private PalestranteDAO palestranteDAO;

    public PalestranteService() {
        this.palestranteDAO = new PalestranteDAO();
    }

    public void adicionarPalestrante(Palestrante palestrante) {
        palestranteDAO.adicionarPalestrante(palestrante);
    }

    public List<Palestrante> listarTodosPalestrantes() {
        return palestranteDAO.listarTodosPalestrantes();
    }

    public Palestrante buscarPalestrantePorId(int id) {
        return palestranteDAO.buscarPalestrantePorId(id);
    }

    public void atualizarPalestrante(Palestrante palestrante) {
        palestranteDAO.atualizarPalestrante(palestrante);
    }

    public void deletarPalestrante(int id) {
        palestranteDAO.deletarPalestrante(id);
    }

    public void fecharConexao() {
        palestranteDAO.fecharConexao();
    }
}
