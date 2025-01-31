CREATE TABLE products.products (
    id text,
    name text,
    description text,
    price text,
    category text,
    PRIMARY KEY (category, id)
);