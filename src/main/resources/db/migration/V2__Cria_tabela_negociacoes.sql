CREATE TABLE IF NOT EXISTS negociacoes (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    tipo_negociacao VARCHAR(10) NOT NULL,
    mercado VARCHAR(15) NOT NULL,
    vencimento DATE,
    instituicao VARCHAR(100),
    codigo_negociacao VARCHAR(6) NOT NULL,
    quantidade SMALLINT NOT NULL CHECK (quantidade > 0),
    preco_unitario REAL NOT NULL,
    CONSTRAINT fk_produtos
        FOREIGN KEY(codigo_negociacao)
            REFERENCES produtos(codigo_negociacao)
)