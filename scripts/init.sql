-- Garante que estamos no banco correto
CREATE DATABASE IF NOT EXISTS financasdb;

USE financasdb;

CREATE TABLE IF NOT EXISTS tb_usuarios (
    `usuario_id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(191) NOT NULL,
    `nome` varchar(100) NOT NULL,
    `senha` varchar(255) NOT NULL,
    `perfil` enum('ADMIN','BASICO') NOT NULL DEFAULT 'BASICO',
    PRIMARY KEY (`usuario_id`),
    UNIQUE KEY `uk_usuario_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS tb_cartoes (
    `cartao_id` bigint NOT NULL AUTO_INCREMENT,
    `usuario_id` bigint NOT NULL,
    `nome` varchar(100) NOT NULL,
    `bandeira` enum('VISA','MASTER') NOT NULL,
    PRIMARY KEY (`cartao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tb_cartoes ADD CONSTRAINT fk_cartoes_usuarios FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(usuario_id);

CREATE TABLE IF NOT EXISTS tb_faturas (
    `fatura_id` bigint NOT NULL AUTO_INCREMENT,
    `cartao_id` bigint NOT NULL,
    `usuario_id` bigint NOT NULL,
    `ano_mes` varchar(6) NOT NULL,
    `s3_bucket` varchar(255) NOT NULL,
    `s3_key` varchar(255) NOT NULL,
    `quantidade_despesas` bigint DEFAULT 0,
    `situacao` enum('PENDENTE','PROCESSANDO', 'CONCLUIDO') NOT NULL DEFAULT 'PENDENTE',
    `data_criacao` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `data_conclusao` timestamp,
    PRIMARY KEY (`fatura_id`),
    CONSTRAINT `uk_faturas_cartao_anomes` UNIQUE (`cartao_id`, `ano_mes`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tb_faturas ADD CONSTRAINT fk_faturas_cartoes FOREIGN KEY (cartao_id) REFERENCES tb_cartoes(cartao_id);
ALTER TABLE tb_faturas ADD CONSTRAINT fk_faturas_usuarios FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(usuario_id);


CREATE TABLE IF NOT EXISTS tb_categorias (
    `categoria_id` bigint NOT NULL AUTO_INCREMENT,
    `nome` varchar(255) NOT NULL,
    `usuario_id` bigint NOT NULL,
    PRIMARY KEY (`categoria_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tb_categorias ADD CONSTRAINT fk_categorias_usuarios FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(usuario_id);


CREATE TABLE IF NOT EXISTS tb_fornecedores (
   `fornecedor_id` bigint NOT NULL AUTO_INCREMENT,
   `usuario_id` bigint NOT NULL,
   `categoria_id` bigint NOT NULL,
   `nome` varchar(255) NOT NULL,
    `palavras_chave` varchar(255) NOT NULL,
    UNIQUE KEY `uk_fonecedor_nome` (`nome`),
    UNIQUE KEY `uk_fonecedor_palavras_chave` (`palavras_chave`),
    PRIMARY KEY (`fornecedor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tb_fornecedores ADD CONSTRAINT fk_fornecedores_usuarios FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(usuario_id);
ALTER TABLE tb_fornecedores ADD CONSTRAINT fk_fornecedores_categorias FOREIGN KEY (categoria_id) REFERENCES tb_categorias(categoria_id);


CREATE TABLE IF NOT EXISTS tb_despesas (
    `despesa_id` bigint NOT NULL AUTO_INCREMENT,
    `fatura_id` bigint NOT NULL,
    `cartao_id` bigint NOT NULL,
    `usuario_id` bigint NOT NULL,
    `categoria_id` bigint,
    `fornecedor_id` bigint,
    `descricao_original` varchar(255) NOT NULL,
    `descricao_processada` varchar(255) NOT NULL,
    `observacao` varchar(255),
    `parcela_atual` bigint,
    `total_parcelas` bigint,
    `sequencia` bigint,
    `data_despesa` date NOT NULL,
    `valor` decimal(15,2) NOT NULL,
    `recorrente` boolean NOT NULL DEFAULT false,
    PRIMARY KEY (`despesa_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tb_despesas ADD CONSTRAINT fk_despesas_fatura FOREIGN KEY (fatura_id) REFERENCES tb_faturas(fatura_id);
ALTER TABLE tb_despesas ADD CONSTRAINT fk_despesas_usuarios FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(usuario_id);
ALTER TABLE tb_despesas ADD CONSTRAINT fk_despesas_cartoes FOREIGN KEY (cartao_id) REFERENCES tb_cartoes(cartao_id);
ALTER TABLE tb_despesas ADD CONSTRAINT fk_despesas_categorias FOREIGN KEY (categoria_id) REFERENCES tb_categorias(categoria_id);
ALTER TABLE tb_despesas ADD CONSTRAINT fk_despesas_fornecedores FOREIGN KEY (fornecedor_id) REFERENCES tb_fornecedores(fornecedor_id);

ALTER TABLE tb_despesas add recorrente boolean NOT NULL DEFAULT  false;

