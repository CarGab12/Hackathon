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

    public Evento(String nomeEvento, String descricao, LocalDateTime dataHora, String local, String fotos) {
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.fotos = fotos;
    }

    public Evento(int id, String nomeEvento, String descricao, LocalDateTime dataHora, String local, String fotos) {
        this.id = id;
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.fotos = fotos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFotos() {
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = (dataHora != null) ? dataHora.format(formatter) : "N/A";
        return "Evento [id=" + id + ", nomeEvento=" + nomeEvento + ", descricao=" + descricao +
                ", dataHora=" + formattedDateTime + ", local=" + local + ", fotos=" + fotos + "]";
    }
}