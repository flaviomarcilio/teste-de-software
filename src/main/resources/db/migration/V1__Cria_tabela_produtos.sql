CREATE TABLE IF NOT EXISTS produtos (
    codigo_negociacao VARCHAR(6) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    codigo_isin VARCHAR(20) NOT NULL,
    tipo_produto VARCHAR(20) NOT NULL,
    administrador VARCHAR(50)
)