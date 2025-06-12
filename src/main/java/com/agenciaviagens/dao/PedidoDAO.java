package com.agenciaviagens.dao;

import com.agenciaviagens.DatabaseConnection;
import com.agenciaviagens.cliente.Cliente;
import com.agenciaviagens.cliente.ClienteEstrangeiro;
import com.agenciaviagens.cliente.ClienteNacional;
import com.agenciaviagens.pacote.PacoteAventura;
import com.agenciaviagens.pacote.PacoteLuxo;
import com.agenciaviagens.pacote.PacoteViagem;
import com.agenciaviagens.pedido.Pedido;
import com.agenciaviagens.servico.ServicoAdicional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private PacoteViagemDAO pacoteViagemDAO = new PacoteViagemDAO();
    private ServicoAdicionalDAO servicoAdicionalDAO = new ServicoAdicionalDAO();

    public void adicionarPedido(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (cliente_id, pacote_id, data_pedido, valor_total) VALUES (?, ?, ?, ?)";
        String sqlPedidoServico = "INSERT INTO pedido_servicos (pedido_id, servico_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                // Obter IDs do cliente e pacote (assumindo que já existem no BD)
                // Para simplificar, vamos buscar pelo documento/nome. Em um sistema real, o ID seria passado.
                int clienteId = getClienteIdByDocumento(pedido.getCliente().getDocumento(), conn);
                int pacoteId = getPacoteIdByNome(pedido.getPacote().getNome(), conn);

                if (clienteId == -1 || pacoteId == -1) {
                    throw new SQLException("Cliente ou Pacote não encontrado no banco de dados.");
                }

                stmtPedido.setInt(1, clienteId);
                stmtPedido.setInt(2, pacoteId);
                stmtPedido.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                stmtPedido.setDouble(4, pedido.getValorTotal());

                stmtPedido.executeUpdate();

                try (ResultSet generatedKeys = stmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedido.setId(generatedKeys.getInt(1));
                    }
                }
            }

            // Adicionar serviços adicionais ao pedido
            try (PreparedStatement stmtPedidoServico = conn.prepareStatement(sqlPedidoServico)) {
                for (ServicoAdicional servico : pedido.getServicosAdicionais()) {
                    stmtPedidoServico.setInt(1, pedido.getId());
                    stmtPedidoServico.setInt(2, servico.getId()); // Assumindo que o ID do serviço já está setado
                    stmtPedidoServico.addBatch();
                }
                stmtPedidoServico.executeBatch();
            }

            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            // Em caso de erro, faz rollback
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            }
            throw e;
        }
    }

    public Pedido buscarPedidoPorId(int id) throws SQLException {
        String sql = "SELECT p.id as pedido_id, p.data_pedido, p.valor_total, " +
                     "c.nome as cliente_nome, c.documento, c.tipo_documento, c.telefone, c.email, " +
                     "pv.nome as pacote_nome, pv.destino, pv.duracao_dias, pv.preco, pv.tipo_pacote, pv.detalhes_especificos " +
                     "FROM pedidos p " +
                     "JOIN clientes c ON p.cliente_id = c.id " +
                     "JOIN pacotes_viagem pv ON p.pacote_id = pv.id " +
                     "WHERE p.id = ?";
        
        Pedido pedido = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Reconstruir Cliente
                    String clienteNome = rs.getString("cliente_nome");
                    String documento = rs.getString("documento");
                    String tipoDocumento = rs.getString("tipo_documento");
                    String telefone = rs.getString("telefone");
                    String email = rs.getString("email");
                    Cliente cliente;
                    if ("CPF".equals(tipoDocumento)) {
                        cliente = new ClienteNacional(clienteNome, documento, telefone, email);
                    } else {
                        cliente = new ClienteEstrangeiro(clienteNome, documento, telefone, email);
                    }

                    // Reconstruir PacoteViagem
                    String pacoteNome = rs.getString("pacote_nome");
                    String destino = rs.getString("destino");
                    int duracao = rs.getInt("duracao_dias");
                    double preco = rs.getDouble("preco");
                    String tipoPacote = rs.getString("tipo_pacote");
                    String detalhesEspecificos = rs.getString("detalhes_especificos");
                    PacoteViagem pacote = null;
                    // Lógica para instanciar o tipo correto de PacoteViagem (Aventura, Luxo, etc.)
                    if ("AVENTURA".equals(tipoPacote)) {
                        String[] detalhes = detalhesEspecificos.split("; ");
                        String atividades = detalhes[0].replace("Atividades: ", "");
                        String nivelDificuldade = detalhes[1].replace("NivelDificuldade: ", "");
                        pacote = new PacoteAventura(pacoteNome, destino, duracao, preco, atividades, nivelDificuldade);
                    } else if ("LUXO".equals(tipoPacote)) {
                        String[] detalhes = detalhesEspecificos.split("; ");
                        String hospedagem = detalhes[0].replace("Hospedagem: ", "");
                        String servicosExclusivos = detalhes[1].replace("ServicosExclusivos: ", "");
                        pacote = new PacoteLuxo(pacoteNome, destino, duracao, preco, hospedagem, servicosExclusivos);
                    }

                    // Buscar serviços adicionais para este pedido
                    List<ServicoAdicional> servicosAdicionais = listarServicosPorPedido(id, conn);

                    pedido = new Pedido(cliente, pacote, servicosAdicionais);
                    pedido.setId(rs.getInt("pedido_id"));
                    pedido.setValorTotal(rs.getDouble("valor_total"));
                }
            }
        }
        return pedido;
    }

    public List<Pedido> listarTodosPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id as pedido_id, p.data_pedido, p.valor_total, " +
                     "c.nome as cliente_nome, c.documento, c.tipo_documento, c.telefone, c.email, " +
                     "pv.nome as pacote_nome, pv.destino, pv.duracao_dias, pv.preco, pv.tipo_pacote, pv.detalhes_especificos " +
                     "FROM pedidos p " +
                     "JOIN clientes c ON p.cliente_id = c.id " +
                     "JOIN pacotes_viagem pv ON p.pacote_id = pv.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Reconstruir Cliente
                String clienteNome = rs.getString("cliente_nome");
                String documento = rs.getString("documento");
                String tipoDocumento = rs.getString("tipo_documento");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                Cliente cliente;
                if ("CPF".equals(tipoDocumento)) {
                    cliente = new ClienteNacional(clienteNome, documento, telefone, email);
                } else {
                    cliente = new ClienteEstrangeiro(clienteNome, documento, telefone, email);
                }

                // Reconstruir PacoteViagem
                String pacoteNome = rs.getString("pacote_nome");
                String destino = rs.getString("destino");
                int duracao = rs.getInt("duracao_dias");
                double preco = rs.getDouble("preco");
                String tipoPacote = rs.getString("tipo_pacote");
                String detalhesEspecificos = rs.getString("detalhes_especificos");
                PacoteViagem pacote = null;
                if ("AVENTURA".equals(tipoPacote)) {
                    String[] detalhes = detalhesEspecificos.split("; ");
                    String atividades = detalhes[0].replace("Atividades: ", "");
                    String nivelDificuldade = detalhes[1].replace("NivelDificuldade: ", "");
                    pacote = new PacoteAventura(pacoteNome, destino, duracao, preco, atividades, nivelDificuldade);
                } else if ("LUXO".equals(tipoPacote)) {
                    String[] detalhes = detalhesEspecificos.split("; ");
                    String hospedagem = detalhes[0].replace("Hospedagem: ", "");
                    String servicosExclusivos = detalhes[1].replace("ServicosExclusivos: ", "");
                    pacote = new PacoteLuxo(pacoteNome, destino, duracao, preco, hospedagem, servicosExclusivos);
                }

                // Buscar serviços adicionais para este pedido
                List<ServicoAdicional> servicosAdicionais = listarServicosPorPedido(rs.getInt("pedido_id"), conn);

                Pedido pedido = new Pedido(cliente, pacote, servicosAdicionais);
                pedido.setId(rs.getInt("pedido_id"));
                pedido.setValorTotal(rs.getDouble("valor_total"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    public void excluirPedido(int id) throws SQLException {
        String sqlDeleteServicos = "DELETE FROM pedido_servicos WHERE pedido_id = ?";
        String sqlDeletePedido = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement stmtServicos = conn.prepareStatement(sqlDeleteServicos)) {
                stmtServicos.setInt(1, id);
                stmtServicos.executeUpdate();
            }

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlDeletePedido)) {
                stmtPedido.setInt(1, id);
                stmtPedido.executeUpdate();
            }

            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            // Em caso de erro, faz rollback
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            }
            throw e;
        }
    }

    // Métodos auxiliares para obter IDs
    private int getClienteIdByDocumento(String documento, Connection conn) throws SQLException {
        String sql = "SELECT id FROM clientes WHERE documento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    private int getPacoteIdByNome(String nome, Connection conn) throws SQLException {
        String sql = "SELECT id FROM pacotes_viagem WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    private List<ServicoAdicional> listarServicosPorPedido(int pedidoId, Connection conn) throws SQLException {
        List<ServicoAdicional> servicos = new ArrayList<>();
        String sql = "SELECT sa.id, sa.nome, sa.descricao, sa.preco FROM servicos_adicionais sa " +
                     "JOIN pedido_servicos ps ON sa.id = ps.servico_id WHERE ps.pedido_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return servicos;
    }
}

