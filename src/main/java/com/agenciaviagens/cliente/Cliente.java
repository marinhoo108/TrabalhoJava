package com.agenciaviagens.cliente;

public abstract class Cliente {
    protected String nome;
    protected String documento; // CPF ou Passaporte
    protected String telefone;
    protected String email;

    public Cliente(String nome, String documento, String telefone, String email) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public abstract void exibirInformacoes();
}