INSERT INTO product_variants(product_id, sku, color, size, price)
SELECT
    p.product_id,
    'SKU-' || p.product_id || '-' || v,
    -- Random color/size
    CASE WHEN random() < 0.5 THEN 'Red' ELSE 'Blue' END,
    CASE WHEN random() < 0.5 THEN 'M' ELSE 'L' END,
    round((random() * 100 + 10)::numeric, 2)
FROM products p,
     generate_series(1, FLOOR(random() * 4 + 2)::int) v;  -- 2–5 variants each
