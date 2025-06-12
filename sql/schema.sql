-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS agencia_viagens;
USE agencia_viagens;

-- Tabela clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    tipo_documento ENUM('CPF', 'PASSAPORTE') NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(255)
);

-- Tabela pacotes_viagem
CREATE TABLE IF NOT EXISTS pacotes_viagem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    duracao_dias INT NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    tipo_pacote VARCHAR(50),
    detalhes_especificos TEXT
);

-- Tabela servicos_adicionais
CREATE TABLE IF NOT EXISTS servicos_adicionais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL
);

-- Tabela pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    pacote_id INT NOT NULL,
    data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (pacote_id) REFERENCES pacotes_viagem(id) ON DELETE CASCADE
);

-- Tabela de junção pedido_servicos
CREATE TABLE IF NOT EXISTS pedido_servicos (
    pedido_id INT NOT NULL,
    servico_id INT NOT NULL,
    PRIMARY KEY (pedido_id, servico_id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (servico_id) REFERENCES servicos_adicionais(id) ON DELETE CASCADE
);

