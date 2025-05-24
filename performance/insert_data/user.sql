INSERT INTO users(full_name, email)
SELECT
    'User ' || g,
    'user' || g || '@example.com'
FROM generate_series(1, 10000) g;
