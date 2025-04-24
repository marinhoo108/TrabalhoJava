package com.agenciaviagens.cliente;

public class ClienteNacional extends Cliente {

    public ClienteNacional(String nome, String cpf, String telefone, String email) {
        super(nome, cpf, telefone, email);

        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório para clientes nacionais.");
        }
    }

    @Override
    public void exibirInformacoes() {
        System.out.println("Cliente Nacional:");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + documento);
        System.out.println("Telefone: " + telefone);
        System.out.println("Email: " + email);
    }
}