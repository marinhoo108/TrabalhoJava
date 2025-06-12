package com.agenciaviagens.pacote;

public class PacoteLuxo extends PacoteViagem {
    private String hospedagem;
    private String servicosExclusivos;

    public PacoteLuxo(String nome, String destino, int duracao, double preco, String hospedagem, String servicosExclusivos) {
        super(nome, destino, duracao, preco);
        this.hospedagem = hospedagem;
        this.servicosExclusivos = servicosExclusivos;
    }

    public String getHospedagem() {
        return hospedagem;
    }

    public void setHospedagem(String hospedagem) {
        this.hospedagem = hospedagem;
    }

    public String getServicosExclusivos() {
        return servicosExclusivos;
    }

    public void setServicosExclusivos(String servicosExclusivos) {
        this.servicosExclusivos = servicosExclusivos;
    }

    @Override
    public String toString() {
        return "PacoteLuxo{" +
                "nome=\'" + getNome() + "\'" +
                ", destino=\'" + getDestino() + "\'" +
                ", duracao=" + getDuracao() +
                ", preco=" + getPreco() +
                ", hospedagem=\'" + hospedagem + "\'" +
                ", servicosExclusivos=\'" + servicosExclusivos + "\'" +
                "}";
    }
}

