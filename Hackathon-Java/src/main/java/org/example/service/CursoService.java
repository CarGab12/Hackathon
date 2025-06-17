package org.example.service;

import org.example.dao.CursoDAO;
import org.example.model.Curso;

import java.util.List;

public class CursoService {
    private CursoDAO cursoDAO;

    public CursoService() {
        this.cursoDAO = new CursoDAO();
    }

    public void adicionarCurso(Curso curso) {
        cursoDAO.adicionarCurso(curso);
    }

    public List<Curso> listarTodosCursos() {
        return cursoDAO.listarTodosCursos();
    }

    public Curso buscarCursoPorId(int id) {
        return cursoDAO.buscarCursoPorId(id);
    }

    public void atualizarCurso(Curso curso) {
        cursoDAO.atualizarCurso(curso);
    }

    public void deletarCurso(int id) {
        cursoDAO.deletarCurso(id);
    }

    public void fecharConexao() {
        cursoDAO.fecharConexao();
    }
}