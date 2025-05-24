CREATE OR REPLACE PROCEDURE insert_bulk_orders(p_total_orders INTEGER)
    LANGUAGE plpgsql
AS $$
DECLARE
    i INT;
    v_user_id INT;
    v_order_id INT;
    v_variant_id INT;
    v_unit_price NUMERIC(10,2);
    v_quantity INT;
    v_total NUMERIC(12,2);
    v_item_count INT;
    j INT;
    v_inventory INT;
BEGIN
    FOR i IN 1..p_total_orders LOOP
            -- Random user
            SELECT user_id INTO v_user_id
            FROM users
            ORDER BY random()
            LIMIT 1;

            -- Insert order
            INSERT INTO orders(user_id, status)
            VALUES (v_user_id, 'PENDING')
            RETURNING order_id INTO v_order_id;

            v_total := 0;
            v_item_count := FLOOR(random() * 5 + 1); -- 1-5 items

            FOR j IN 1..v_item_count LOOP
                    -- Chọn 1 variant ngẫu nhiên
                    SELECT pv.variant_id, pv.price, COALESCE(i.quantity, 0)
                    INTO v_variant_id, v_unit_price, v_inventory
                    FROM product_variants pv
                             LEFT JOIN inventory i ON i.variant_id = pv.variant_id
                    ORDER BY random()
                    LIMIT 1;

                    v_quantity := FLOOR(random() * 3 + 1); -- 1–3 sp mỗi loại

                    -- Nếu còn tồn kho thì mới cho đặt
                    IF v_inventory >= v_quantity THEN
                        -- Insert item
                        INSERT INTO order_items(order_id, variant_id, quantity, unit_price)
                        VALUES (v_order_id, v_variant_id, v_quantity, v_unit_price);

                        -- Cập nhật tồn kho
                        UPDATE inventory
                        SET quantity = quantity - v_quantity
                        WHERE variant_id = v_variant_id;

                        -- Cộng vào tổng tiền
                        v_total := v_total + v_unit_price * v_quantity;
                    END IF;
                END LOOP;

            -- Cập nhật tổng tiền
            UPDATE orders
            SET total_amount = v_total
            WHERE order_id = v_order_id;

            -- Ghi log trạng thái
            INSERT INTO order_status_history(order_id, status, note)
            VALUES (v_order_id, 'PENDING', 'Order created');
        END LOOP;
END;
$$;
