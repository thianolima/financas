-- financasdb.tb_usuarios definition

CREATE TABLE `tb_usuarios` (
    `usuario_id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(191) NOT NULL,
    `nome` varchar(100) NOT NULL,
    `senha` varchar(255) NOT NULL,
    `perfil` enum('ADMIN','BASICO') NOT NULL DEFAULT 'BASICO',
    PRIMARY KEY (`usuario_id`),
    UNIQUE KEY `uk_usuario_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- financasdb.tb_cartoes definition

CREATE TABLE `tb_cartoes` (
    `cartao_id` bigint NOT NULL AUTO_INCREMENT,
    `usuario_id` bigint NOT NULL,
    `nome` varchar(100) NOT NULL,
    `bandeira` enum('VISA','MASTER') NOT NULL,
    `dia_vencimento` bigint NOT NULL,
    PRIMARY KEY (`cartao_id`),
    KEY `fk_cartoes_usuarios` (`usuario_id`),
    CONSTRAINT `fk_cartoes_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- financasdb.tb_categorias definition

CREATE TABLE `tb_categorias` (
    `categoria_id` bigint NOT NULL AUTO_INCREMENT,
    `nome` varchar(255) NOT NULL,
    `usuario_id` bigint NOT NULL,
    PRIMARY KEY (`categoria_id`),
    KEY `fk_categorias_usuarios` (`usuario_id`),
    CONSTRAINT `fk_categorias_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- financasdb.tb_faturas definition

CREATE TABLE `tb_faturas` (
    `fatura_id` bigint NOT NULL AUTO_INCREMENT,
    `cartao_id` bigint NOT NULL,
    `usuario_id` bigint NOT NULL,
    `ano_mes` varchar(6) NOT NULL,
    `s3_bucket` varchar(255) NOT NULL,
    `s3_key` varchar(255) NOT NULL,
    `quantidade_despesas` bigint DEFAULT '0',
    `situacao` enum('PENDENTE','PROCESSANDO','CONCLUIDO') NOT NULL DEFAULT 'PENDENTE',
    `data_criacao` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_conclusao` timestamp NULL DEFAULT NULL,
    `data_vencimento` date NOT NULL,
    PRIMARY KEY (`fatura_id`),
    UNIQUE KEY `uk_faturas_cartao_anomes` (`cartao_id`,`ano_mes`),
    KEY `fk_faturas_usuarios` (`usuario_id`),
    CONSTRAINT `fk_faturas_cartoes` FOREIGN KEY (`cartao_id`) REFERENCES `tb_cartoes` (`cartao_id`),
    CONSTRAINT `fk_faturas_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- financasdb.tb_fornecedores definition

CREATE TABLE `tb_fornecedores` (
    `fornecedor_id` bigint NOT NULL AUTO_INCREMENT,
    `usuario_id` bigint NOT NULL,
    `categoria_id` bigint NOT NULL,
    `nome` varchar(255) NOT NULL,
    `palavras_chave` varchar(255) NOT NULL,
    PRIMARY KEY (`fornecedor_id`),
    UNIQUE KEY `uk_fonecedor_nome` (`nome`),
    UNIQUE KEY `uk_fonecedor_palavras_chave` (`palavras_chave`),
    KEY `fk_fornecedores_usuarios` (`usuario_id`),
    KEY `fk_fornecedores_categorias` (`categoria_id`),
    CONSTRAINT `fk_fornecedores_categorias` FOREIGN KEY (`categoria_id`) REFERENCES `tb_categorias` (`categoria_id`),
    CONSTRAINT `fk_fornecedores_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- financasdb.tb_despesas definition

CREATE TABLE `tb_despesas` (
    `despesa_id` bigint NOT NULL AUTO_INCREMENT,
    `fatura_id` bigint NOT NULL,
    `cartao_id` bigint NOT NULL,
    `usuario_id` bigint NOT NULL,
    `categoria_id` bigint DEFAULT NULL,
    `fornecedor_id` bigint DEFAULT NULL,
    `descricao_original` varchar(255) NOT NULL,
    `descricao_processada` varchar(255) NOT NULL,
    `observacao` varchar(255) DEFAULT NULL,
    `parcela_atual` bigint DEFAULT NULL,
    `total_parcelas` bigint DEFAULT NULL,
    `sequencia` bigint DEFAULT NULL,
    `data_despesa` date NOT NULL,
    `valor` decimal(15,2) NOT NULL,
    `recorrente` tinyint(1) NOT NULL DEFAULT '0',
    `data_vencimento` date NOT NULL,
    PRIMARY KEY (`despesa_id`),
    UNIQUE KEY `uk_tb_despesas_fatura_sequencia` (`fatura_id`,`sequencia`),
    KEY `fk_despesas_usuarios` (`usuario_id`),
    KEY `fk_despesas_cartoes` (`cartao_id`),
    KEY `fk_despesas_categorias` (`categoria_id`),
    KEY `fk_despesas_fornecedores` (`fornecedor_id`),
    CONSTRAINT `fk_despesas_cartoes` FOREIGN KEY (`cartao_id`) REFERENCES `tb_cartoes` (`cartao_id`),
    CONSTRAINT `fk_despesas_categorias` FOREIGN KEY (`categoria_id`) REFERENCES `tb_categorias` (`categoria_id`),
    CONSTRAINT `fk_despesas_fatura` FOREIGN KEY (`fatura_id`) REFERENCES `tb_faturas` (`fatura_id`),
    CONSTRAINT `fk_despesas_fornecedores` FOREIGN KEY (`fornecedor_id`) REFERENCES `tb_fornecedores` (`fornecedor_id`),
    CONSTRAINT `fk_despesas_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `tb_usuarios` (`usuario_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6534 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;