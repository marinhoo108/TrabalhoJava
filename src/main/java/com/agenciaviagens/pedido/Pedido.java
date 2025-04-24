package com.agenciaviagens.pedido;

import com.agenciaviagens.cliente.Cliente;
import com.agenciaviagens.servico.ServicoAdicional;
import com.agenciaviagens.pacote.PacoteViagem;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private PacoteViagem pacote;
    private List<ServicoAdicional> servicosAdicionais;
    private double valorTotal;

    // Construtor
    public Pedido(Cliente cliente, PacoteViagem pacote, List<ServicoAdicional> servicosAdicionais) {
        this.cliente = cliente;
        this.pacote = pacote;
        this.servicosAdicionais = servicosAdicionais;
        this.valorTotal = calcularValorTotal();  // Calcula o valor total automaticamente
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PacoteViagem getPacote() {
        return pacote;
    }

    public void setPacote(PacoteViagem pacote) {
        this.pacote = pacote;
    }

    public List<ServicoAdicional> getServicosAdicionais() {
        return servicosAdicionais;
    }

    public void setServicosAdicionais(List<ServicoAdicional> servicosAdicionais) {
        this.servicosAdicionais = servicosAdicionais;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método para calcular o valor total do pedido
    public double calcularValorTotal() {
        double total = pacote.getPreco(); // Preço do pacote
        for (ServicoAdicional servico : servicosAdicionais) {
            total += servico.getPreco(); // Adiciona o preço dos serviços adicionais
        }
        return total;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() +
                ", pacote=" + pacote.getNome() +
                ", servicosAdicionais=" + servicosAdicionais +
                ", valorTotal=" + valorTotal +
                '}';
    }
}