-- Criar a tabela contas
CREATE TABLE contas (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT DEFAULT 0,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(15, 2) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    situacao VARCHAR(50)
);