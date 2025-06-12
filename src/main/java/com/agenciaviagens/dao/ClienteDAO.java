package com.agenciaviagens.dao;

import com.agenciaviagens.DatabaseConnection;
import com.agenciaviagens.cliente.Cliente;
import com.agenciaviagens.cliente.ClienteEstrangeiro;
import com.agenciaviagens.cliente.ClienteNacional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, documento, tipo_documento, telefone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getDocumento());
            
            if (cliente instanceof ClienteNacional) {
                stmt.setString(3, "CPF");
            } else if (cliente instanceof ClienteEstrangeiro) {
                stmt.setString(3, "PASSAPORTE");
            } else {
                stmt.setString(3, "DESCONHECIDO"); // Ou lançar uma exceção
            }
            
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEmail());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // cliente.setId(generatedKeys.getInt(1)); // Se a classe Cliente tivesse um setId
                }
            }
        }
    }

    public Cliente buscarClientePorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String doc = rs.getString("documento");
                    String tipoDoc = rs.getString("tipo_documento");
                    String telefone = rs.getString("telefone");
                    String email = rs.getString("email");

                    if (tipoDoc.equals("CPF")) {
                        return new ClienteNacional(nome, doc, telefone, email);
                    } else if (tipoDoc.equals("PASSAPORTE")) {
                        return new ClienteEstrangeiro(nome, doc, telefone, email);
                    }
                }
            }
        }
        return null;
    }

    public List<Cliente> listarTodosClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String doc = rs.getString("documento");
                String tipoDoc = rs.getString("tipo_documento");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");

                if (tipoDoc.equals("CPF")) {
                    clientes.add(new ClienteNacional(nome, doc, telefone, email));
                } else if (tipoDoc.equals("PASSAPORTE")) {
                    clientes.add(new ClienteEstrangeiro(nome, doc, telefone, email));
                }
            }
        }
        return clientes;
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE documento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDocumento());

            stmt.executeUpdate();
        }
    }

    public void excluirCliente(String documento) throws SQLException {
        String sql = "DELETE FROM clientes WHERE documento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            stmt.executeUpdate();
        }
    }
}

