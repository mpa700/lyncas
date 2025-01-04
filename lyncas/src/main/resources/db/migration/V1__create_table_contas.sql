-- Criar a tabela contas
CREATE TABLE contas (
    id BIGSERIAL PRIMARY KEY,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor NUMERIC(10, 2) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    situacao VARCHAR(20) NOT NULL
);