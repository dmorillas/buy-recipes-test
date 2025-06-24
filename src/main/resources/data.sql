DELETE FROM cart_items;
DELETE FROM recipe_product;
DELETE FROM cart;
DELETE FROM recipe;
DELETE FROM product;

INSERT INTO product (id, name, price_in_cents) VALUES (1, 'Olive Oil', 10);
INSERT INTO product (id, name, price_in_cents) VALUES (2, 'Potato', 20);
INSERT INTO product (id, name, price_in_cents) VALUES (3, 'Eggs', 30);
INSERT INTO product (id, name, price_in_cents) VALUES (4, 'Onion', 40);
INSERT INTO product (id, name, price_in_cents) VALUES (5, 'Salt', 50);
INSERT INTO product (id, name, price_in_cents) VALUES (6, 'Spaghetti', 60);
INSERT INTO product (id, name, price_in_cents) VALUES (7, 'Garlic', 70);
INSERT INTO product (id, name, price_in_cents) VALUES (8, 'Pepper flakes', 80);
INSERT INTO product (id, name, price_in_cents) VALUES (9, 'Parmigiano-Reggiano cheese', 90);

INSERT INTO recipe (id, name) VALUES (1, 'Spanish Omelette');

INSERT INTO recipe_product (recipe_id, product_id) VALUES (1, 1);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (1, 2);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (1, 3);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (1, 4);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (1, 5);

INSERT INTO recipe (id, name) VALUES (2, 'Spaghetti Aglio e Olio');

INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 1);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 5);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 6);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 7);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 8);
INSERT INTO recipe_product (recipe_id, product_id) VALUES (2, 9);
