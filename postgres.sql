CREATE TABLE promotion (
  id SERIAL PRIMARY KEY,
  label VARCHAR(255) NOT NULL,
  price NUMERIC(10, 2) NOT NULL,
  description VARCHAR(255) NOT NULL,
  category VARCHAR(255) NOT NULL
);