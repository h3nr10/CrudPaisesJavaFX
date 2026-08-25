-- Execute este script no PostgreSQL (ex.: via psql ou pgAdmin) antes de rodar a aplicação.

-- 1) Crie o banco (rode esta linha separadamente, fora de uma transação, conectado ao banco "postgres"):
-- CREATE DATABASE "Paises";

-- 2) Conecte-se ao banco "Paises" e rode o restante:
CREATE TABLE IF NOT EXISTS paises (
    id                     BIGSERIAL PRIMARY KEY,
    nome                   VARCHAR(100) NOT NULL,
    sigla                  VARCHAR(10)  NOT NULL,
    capital                VARCHAR(100),
    area_km2               DOUBLE PRECISION,
    pib_ppc_bilhoes        DOUBLE PRECISION,
    populacao              INTEGER,
    indice_poder_militar   DOUBLE PRECISION
);
