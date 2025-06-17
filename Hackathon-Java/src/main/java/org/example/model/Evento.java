package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private int id;
    private String nomeEvento;
    private String descricao;
    private LocalDateTime dataHora;
    private String local;
    private String fotos;
    private int palestranteId;
    private Palestrante palestrante;
    private int cursoId;
    private Curso curso;


    public Evento(String nomeEvento, String descricao, LocalDateTime dataHora, String local, String fotos, int palestranteId, int cursoId) {
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.fotos = fotos;
        this.palestranteId = palestranteId;
        this.cursoId = cursoId;
    }


    public Evento(int id, String nomeEvento, String descricao, LocalDateTime dataHora, String local, String fotos, int palestranteId, int cursoId) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.fotos = fotos;
        this.palestranteId = palestranteId;
        this.cursoId = cursoId;
    }


    public Evento(int id, String nomeEvento, String descricao, LocalDateTime dataHora, String local, String fotos, Palestrante palestrante, Curso curso) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.fotos = fotos;
        this.palestrante = palestrante;
        this.palestranteId = (palestrante != null) ? palestrante.getId() : 0;
        this.curso = curso;
        this.cursoId = (curso != null) ? curso.getId() : 0;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeEvento() { return nomeEvento; }
    public void setNomeEvento(String nomeEvento) { this.nomeEvento = nomeEvento; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getFotos() { return fotos; }
    public void setFotos(String fotos) { this.fotos = fotos; }
    public int getPalestranteId() { return palestranteId; }
    public void setPalestranteId(int palestranteId) { this.palestranteId = palestranteId; }
    public Palestrante getPalestrante() { return palestrante; }
    public void setPalestrante(Palestrante palestrante) {
        this.palestrante = palestrante;
        this.palestranteId = (palestrante != null) ? palestrante.getId() : 0;
    }
    public int getCursoId() { return cursoId; }
    public void setCursoId(int cursoId) { this.cursoId = cursoId; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) {
        this.curso = curso;
        this.cursoId = (curso != null) ? curso.getId() : 0;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = (dataHora != null) ? dataHora.format(formatter) : "N/A";
        String palestranteNome = (palestrante != null) ? palestrante.getNome() : "N/A";
        String cursoNome = (curso != null) ? curso.getNome() : "N/A";

        return "Evento [id=" + id + ", nomeEvento=" + nomeEvento + ", descricao=" + descricao +
                ", dataHora=" + formattedDateTime + ", local=" + local + ", fotos=" + fotos +
                ", palestrante=" + palestranteNome + ", curso=" + cursoNome + "]";
    }
}