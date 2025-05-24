INSERT INTO suppliers(name, contact_email)
SELECT
    'Supplier ' || g,
    'supplier' || g || '@vendor.com'
FROM generate_series(1, 200) g;
