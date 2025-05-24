INSERT INTO products(name, description, category_id, supplier_id)
SELECT
    'Product ' || g,
    'Description for product ' || g,
    (SELECT category_id FROM categories ORDER BY random() LIMIT 1),
    (SELECT supplier_id FROM suppliers ORDER BY random() LIMIT 1)
FROM generate_series(1, 5000) g;
