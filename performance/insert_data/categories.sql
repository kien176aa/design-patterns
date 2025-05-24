-- Danh mục cha
INSERT INTO categories(name) VALUES
                                 ('Electronics'), ('Clothing'), ('Books');

-- Danh mục con
INSERT INTO categories(name, parent_id)
SELECT
    'SubCategory ' || g,
    (SELECT category_id FROM categories ORDER BY random() LIMIT 1)
FROM generate_series(1, 30) g;
