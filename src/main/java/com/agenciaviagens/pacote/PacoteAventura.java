package com.agenciaviagens.pacote;

public class PacoteAventura extends PacoteViagem {
    private String atividades;
    private String nivelDificuldade;

    public PacoteAventura(String nome, String destino, int duracao, double preco, String atividades, String nivelDificuldade) {
        super(nome, destino, duracao, preco);
        this.atividades = atividades;
        this.nivelDificuldade = nivelDificuldade;
    }

    public String getAtividades() {
        return atividades;
    }

    public void setAtividades(String atividades) {
        this.atividades = atividades;
    }

    public String getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(String nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    @Override
    public String toString() {
        return "PacoteAventura{" +
                "nome='" + getNome() + "'" +
                ", destino='" + getDestino() + "'" +
                ", duracao=" + getDuracao() +
                ", preco=" + getPreco() +
                ", atividades='" + atividades + "'" +
                ", nivelDificuldade='" + nivelDificuldade + "'" +
                "}";
    }
}

