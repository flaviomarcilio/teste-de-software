CREATE TABLE IF NOT EXISTS movimentacoes (
    id SERIAL PRIMARY KEY,
    tipo_transacao VARCHAR(10) NOT NULL,
    data DATE NOT NULL,
    tipo_movimentacao VARCHAR(50) NOT NULL,
    codigo_negociacao VARCHAR(6) NOT NULL,
    instituicao VARCHAR(100) NOT NULL,
    quantidade SMALLINT NOT NULL CHECK (quantidade > 0),
    preco_unitario REAL NOT NULL,
    CONSTRAINT fk_produtos
            FOREIGN KEY(codigo_negociacao)
                REFERENCES produtos(codigo_negociacao)
)