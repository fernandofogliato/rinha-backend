ALTER DATABASE rinha SET TIMEZONE TO 'UTC';

CREATE UNLOGGED TABLE  customer (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    "limit" BIGINT NOT NULL,
    balance BIGINT NOT NULL DEFAULT 0
);

CREATE UNLOGGED TABLE "transaction" (
    transaction_id SERIAL PRIMARY KEY,
    value BIGINT NOT NULL,
    description VARCHAR(10) NOT NULL,
    type VARCHAR(1) NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO customer (name, "limit")
VALUES
('o barato sai caro', 1000 * 100),
('zan corp ltda', 800 * 100),
('les cruders', 10000 * 100),
('padaria joia de cocaia', 100000 * 100),
('kid mais', 5000 * 100);