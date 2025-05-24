explain analyze select sum(o.total_amount), u.full_name, u.email, sum(o.user_id),
       avg(o.total_amount), max(o.order_date)
from orders o
left join users u on o.user_id = u.user_id
where DATE_PART('month', o.order_date) = 4
group by u.full_name, u.email
order by sum(o.total_amount) desc 
limit 10;



select sum(oi.quantity), p.product_id from order_items oi
left join product_variants pv on oi.variant_id = pv.variant_id
left join products p on pv.product_id = p.product_id
group by p.product_id
order by sum(oi.quantity) desc
limit 5;

select DATE_PART('month', o.order_date) from orders o;


explain analyze SELECT
    o.order_id,
    o.order_date,
    u.full_name AS customer_name,
    u.email,
    SUM(oi.quantity) AS total_quantity,
    COUNT(DISTINCT p.product_id) AS total_product_types,
    SUM(oi.quantity * oi.unit_price) AS total_order_amount,
    ARRAY_AGG(DISTINCT p.name) AS product_names,
    ARRAY_AGG(DISTINCT s.name) AS supplier_names,
    ARRAY_AGG(DISTINCT c.name) AS category_names,
    o.status
FROM orders o
         JOIN users u ON o.user_id = u.user_id
         JOIN order_items oi ON o.order_id = oi.order_id
         JOIN product_variants pv ON oi.variant_id = pv.variant_id
         JOIN products p ON pv.product_id = p.product_id
         LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id
         LEFT JOIN categories c ON p.category_id = c.category_id
WHERE o.order_date >= '2025-04-01' AND o.order_date < '2025-05-01'
GROUP BY
    o.order_id, o.order_date, u.full_name, u.email,
    o.status
ORDER BY o.order_date;


call insert_bulk_orders(100);


select * from orders order by order_id desc;
