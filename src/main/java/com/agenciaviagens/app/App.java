package com.agenciaviagens.app;

import com.agenciaviagens.DatabaseConnection;
import com.agenciaviagens.cliente.Cliente;
import com.agenciaviagens.cliente.ClienteEstrangeiro;
import com.agenciaviagens.cliente.ClienteNacional;
import com.agenciaviagens.pacote.PacoteViagem;
import com.agenciaviagens.pedido.Pedido;
import com.agenciaviagens.servico.ServicoAdicional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            // Aqui você pode adicionar a lógica principal do seu aplicativo, como um menu de opções
            // Exemplo:
            // menuPrincipal();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos para o menu principal e submenus (a serem implementados)
    private static void menuPrincipal() {
        int opcao;
        do {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Pacotes de Viagem");
            System.out.println("3. Gerenciar Serviços Adicionais");
            System.out.println("4. Realizar Pedido");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuPacotes();
                    break;
                case 3:
                    menuServicos();
                    break;
                case 4:
                    realizarPedido();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void menuClientes() {
        // Implementar lógica para gerenciar clientes
        System.out.println("\n--- Gerenciar Clientes ---");
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    private static void menuPacotes() {
        // Implementar lógica para gerenciar pacotes
        System.out.println("\n--- Gerenciar Pacotes de Viagem ---");
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    private static void menuServicos() {
        // Implementar lógica para gerenciar serviços
        System.out.println("\n--- Gerenciar Serviços Adicionais ---");
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    private static void realizarPedido() {
        // Implementar lógica para realizar pedidos
        System.out.println("\n--- Realizar Pedido ---");
        System.out.println("Funcionalidade em desenvolvimento.");
    }
}


