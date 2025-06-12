package com.agenciaviagens.dao;

import com.agenciaviagens.DatabaseConnection;
import com.agenciaviagens.servico.ServicoAdicional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoAdicionalDAO {

    public void adicionarServico(ServicoAdicional servico) throws SQLException {
        String sql = "INSERT INTO servicos_adicionais (nome, descricao, preco) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servico.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public ServicoAdicional buscarServicoPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM servicos_adicionais WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String descricao = rs.getString("descricao");
                    double preco = rs.getDouble("preco");
                    ServicoAdicional servico = new ServicoAdicional(nome, descricao, preco);
                    servico.setId(id);
                    return servico;
                }
            }
        }
        return null;
    }

    public List<ServicoAdicional> listarTodosServicos() throws SQLException {
        List<ServicoAdicional> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servicos_adicionais";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                ServicoAdicional servico = new ServicoAdicional(nome, descricao, preco);
                servico.setId(id);
                servicos.add(servico);
            }
        }
        return servicos;
    }

    public void atualizarServico(ServicoAdicional servico) throws SQLException {
        String sql = "UPDATE servicos_adicionais SET nome = ?, descricao = ?, preco = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());
            stmt.setInt(4, servico.getId());

            stmt.executeUpdate();
        }
    }

    public void excluirServico(int id) throws SQLException {
        String sql = "DELETE FROM servicos_adicionais WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

