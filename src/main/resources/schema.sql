-- Drop existing tables
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS recipe_product;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    price_in_cents INTEGER NOT NULL
);

CREATE TABLE recipe (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE recipe_product (
    recipe_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    PRIMARY KEY (recipe_id, product_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE cart (
    id INTEGER PRIMARY KEY,
    total_in_cents INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE cart_items (
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
);
