CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    promotion_start_date DATE,
    promotion_end_date DATE,
    promotion_discount DECIMAL(5, 2)
);