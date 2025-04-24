package com.agenciaviagens.cliente;

public class ClienteEstrangeiro extends Cliente {

    public ClienteEstrangeiro(String nome, String passaporte, String telefone, String email) {
        super(nome, passaporte, telefone, email);

        if (passaporte == null || passaporte.isEmpty()) {
            throw new IllegalArgumentException("Passaporte é obrigatório para clientes estrangeiros.");
        }
    }

    @Override
    public void exibirInformacoes() {
        System.out.println("Cliente Estrangeiro:");
        System.out.println("Nome: " + nome);
        System.out.println("Passaporte: " + documento);
        System.out.println("Telefone: " + telefone);
        System.out.println("Email: " + email);
    }
}