INSERT INTO inventory(variant_id, quantity, location)
SELECT
    variant_id,
    FLOOR(random() * 100 + 10)::int,
    'Warehouse ' || FLOOR(random() * 5 + 1)
FROM product_variants;
