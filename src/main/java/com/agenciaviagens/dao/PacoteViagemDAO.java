package com.agenciaviagens.dao;

import com.agenciaviagens.DatabaseConnection;
import com.agenciaviagens.pacote.PacoteAventura;
import com.agenciaviagens.pacote.PacoteLuxo;
import com.agenciaviagens.pacote.PacoteViagem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacoteViagemDAO {

    public void adicionarPacote(PacoteViagem pacote) throws SQLException {
        String sql = "INSERT INTO pacotes_viagem (nome, destino, duracao_dias, preco, tipo_pacote, detalhes_especificos) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pacote.getNome());
            stmt.setString(2, pacote.getDestino());
            stmt.setInt(3, pacote.getDuracao());
            stmt.setDouble(4, pacote.getPreco());

            String tipoPacote = null;
            String detalhesEspecificos = null;

            if (pacote instanceof PacoteAventura) {
                tipoPacote = "AVENTURA";
                PacoteAventura pa = (PacoteAventura) pacote;
                detalhesEspecificos = "Atividades: " + pa.getAtividades() + "; NivelDificuldade: " + pa.getNivelDificuldade();
            } else if (pacote instanceof PacoteLuxo) {
                tipoPacote = "LUXO";
                PacoteLuxo pl = (PacoteLuxo) pacote;
                detalhesEspecificos = "Hospedagem: " + pl.getHospedagem() + "; ServicosExclusivos: " + pl.getServicosExclusivos();
            } // Adicionar mais tipos de pacote aqui

            stmt.setString(5, tipoPacote);
            stmt.setString(6, detalhesEspecificos);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // pacote.setId(generatedKeys.getInt(1)); // Se a classe PacoteViagem tivesse um setId
                }
            }
        }
    }

    public PacoteViagem buscarPacotePorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM pacotes_viagem WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String destino = rs.getString("destino");
                    int duracao = rs.getInt("duracao_dias");
                    double preco = rs.getDouble("preco");
                    String tipoPacote = rs.getString("tipo_pacote");
                    String detalhesEspecificos = rs.getString("detalhes_especificos");

                    if ("AVENTURA".equals(tipoPacote)) {
                        String[] detalhes = detalhesEspecificos.split("; ");
                        String atividades = detalhes[0].replace("Atividades: ", "");
                        String nivelDificuldade = detalhes[1].replace("NivelDificuldade: ", "");
                        return new PacoteAventura(nome, destino, duracao, preco, atividades, nivelDificuldade);
                    } else if ("LUXO".equals(tipoPacote)) {
                        String[] detalhes = detalhesEspecificos.split("; ");
                        String hospedagem = detalhes[0].replace("Hospedagem: ", "");
                        String servicosExclusivos = detalhes[1].replace("ServicosExclusivos: ", "");
                        return new PacoteLuxo(nome, destino, duracao, preco, hospedagem, servicosExclusivos);
                    }
                }
            }
        }
        return null;
    }

    public List<PacoteViagem> listarTodosPacotes() throws SQLException {
        List<PacoteViagem> pacotes = new ArrayList<>();
        String sql = "SELECT * FROM pacotes_viagem";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                String destino = rs.getString("destino");
                int duracao = rs.getInt("duracao_dias");
                double preco = rs.getDouble("preco");
                String tipoPacote = rs.getString("tipo_pacote");
                String detalhesEspecificos = rs.getString("detalhes_especificos");

                if ("AVENTURA".equals(tipoPacote)) {
                    String[] detalhes = detalhesEspecificos.split("; ");
                    String atividades = detalhes[0].replace("Atividades: ", "");
                    String nivelDificuldade = detalhes[1].replace("NivelDificuldade: ", "");
                    pacotes.add(new PacoteAventura(nome, destino, duracao, preco, atividades, nivelDificuldade));
                } else if ("LUXO".equals(tipoPacote)) {
                    String[] detalhes = detalhesEspecificos.split("; ");
                    String hospedagem = detalhes[0].replace("Hospedagem: ", "");
                    String servicosExclusivos = detalhes[1].replace("ServicosExclusivos: ", "");
                    pacotes.add(new PacoteLuxo(nome, destino, duracao, preco, hospedagem, servicosExclusivos));
                }
            }
        }
        return pacotes;
    }

    public void atualizarPacote(PacoteViagem pacote) throws SQLException {
        String sql = "UPDATE pacotes_viagem SET destino = ?, duracao_dias = ?, preco = ?, tipo_pacote = ?, detalhes_especificos = ? WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pacote.getDestino());
            stmt.setInt(2, pacote.getDuracao());
            stmt.setDouble(3, pacote.getPreco());

            String tipoPacote = null;
            String detalhesEspecificos = null;

            if (pacote instanceof PacoteAventura) {
                tipoPacote = "AVENTURA";
                PacoteAventura pa = (PacoteAventura) pacote;
                detalhesEspecificos = "Atividades: " + pa.getAtividades() + "; NivelDificuldade: " + pa.getNivelDificuldade();
            } else if (pacote instanceof PacoteLuxo) {
                tipoPacote = "LUXO";
                PacoteLuxo pl = (PacoteLuxo) pacote;
                detalhesEspecificos = "Hospedagem: " + pl.getHospedagem() + "; ServicosExclusivos: " + pl.getServicosExclusivos();
            }

            stmt.setString(4, tipoPacote);
            stmt.setString(5, detalhesEspecificos);
            stmt.setString(6, pacote.getNome());

            stmt.executeUpdate();
        }
    }

    public void excluirPacote(String nome) throws SQLException {
        String sql = "DELETE FROM pacotes_viagem WHERE nome = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.executeUpdate();
        }
    }
}

