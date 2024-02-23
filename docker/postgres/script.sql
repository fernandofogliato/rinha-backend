ALTER DATABASE rinha SET TIMEZONE TO 'UTC';

CREATE UNLOGGED TABLE customer (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    "limit" INTEGER NOT NULL
);

CREATE UNLOGGED TABLE "transaction" (
    transaction_id SERIAL PRIMARY KEY,
    value INTEGER NOT NULL,
    description VARCHAR(10) NOT NULL,
    type VARCHAR(1) NOT NULL,
    customer_id INTEGER NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE UNLOGGED TABLE balance (
	balance_id SERIAL PRIMARY KEY,
	customer_id INTEGER NOT NULL,
	balance INTEGER NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO customer (name, "limit")
VALUES
('o barato sai caro', 1000 * 100),
('zan corp ltda', 800 * 100),
('les cruders', 10000 * 100),
('padaria joia de cocaia', 100000 * 100),
('kid mais', 5000 * 100);

INSERT INTO balance (customer_id, balance)
SELECT customer_id, 0 AS balance
FROM customer;