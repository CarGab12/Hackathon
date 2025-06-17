package org.example.model;

public class Palestrante {
    private int id;
    private String nome;
    private String minicurriculo;

    public Palestrante(String nome, String minicurriculo) {
        this.nome = nome;
        this.minicurriculo = minicurriculo;
    }

    public Palestrante(int id, String nome, String minicurriculo) {
        this.id = id;
        this.nome = nome;
        this.minicurriculo = minicurriculo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMinicurriculo() {
        return minicurriculo;
    }

    public void setMinicurriculo(String minicurriculo) {
        this.minicurriculo = minicurriculo;
    }

    @Override
    public String toString() {
        return nome;
    }
}

