package com.dp;

import java.sql.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelJavaInserter {

    static final String DB_URL = "jdbc:postgresql://localhost:5433/performance";
    static final String DB_USER = "postgres";
    static final String DB_PASS = "123456";

    static final int TOTAL_THREADS = 10;
    static final int ORDERS_PER_THREAD = 10000;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREADS);
        for (int i = 0; i < TOTAL_THREADS; i++) {
            int threadId = i + 1;
            executor.execute(() -> {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                    conn.setAutoCommit(false);
                    insertBulkOrders(conn, ORDERS_PER_THREAD, threadId);
                    conn.commit();
                    System.out.printf("[Thread %d] ✅ Commit hoàn tất%n", threadId);
                } catch (Exception e) {
                    System.err.printf("[Thread %d] ❌ Lỗi: %s%n", threadId, e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

    private static void insertBulkOrders(Connection conn, int totalOrders, int threadId) throws SQLException {
        Random rand = new Random();
        for (int i = 0; i < totalOrders; i++) {
            int userId = getRandomUserId(conn);
            PreparedStatement insertOrder = conn.prepareStatement(
                    "INSERT INTO orders(user_id, status, order_date) VALUES (?, 'PENDING', now() - (random() * interval '30 days')) RETURNING order_id");
            insertOrder.setInt(1, userId);
            ResultSet rs = insertOrder.executeQuery();
            rs.next();
            int orderId = rs.getInt(1);
            rs.close();
            insertOrder.close();

            System.out.printf("[Thread %d] ➕ Insert order #%d for user %d%n", threadId, orderId, userId);

            double total = 0.0;
            int itemCount = rand.nextInt(5) + 1;
            for (int j = 0; j < itemCount; j++) {
                PreparedStatement variantStmt = conn.prepareStatement(
                        "SELECT pv.variant_id, pv.price, COALESCE(i.quantity, 0) " +
                                "FROM product_variants pv LEFT JOIN inventory i ON i.variant_id = pv.variant_id " +
                                "ORDER BY random() LIMIT 1");
                ResultSet variantRS = variantStmt.executeQuery();
                if (!variantRS.next()) continue;

                int variantId = variantRS.getInt(1);
                double unitPrice = variantRS.getDouble(2);
                int inventory = variantRS.getInt(3);
                variantRS.close();
                variantStmt.close();

                int quantity = rand.nextInt(3) + 1;
                if (inventory >= quantity) {
                    PreparedStatement itemInsert = conn.prepareStatement(
                            "INSERT INTO order_items(order_id, variant_id, quantity, unit_price) VALUES (?, ?, ?, ?)");
                    itemInsert.setInt(1, orderId);
                    itemInsert.setInt(2, variantId);
                    itemInsert.setInt(3, quantity);
                    itemInsert.setDouble(4, unitPrice);
                    itemInsert.executeUpdate();
                    itemInsert.close();

                    System.out.printf("[Thread %d]   🛒 Add item variant %d x %d (%.2f) to order #%d%n",
                            threadId, variantId, quantity, unitPrice, orderId);

                    total += quantity * unitPrice;
                }
            }

            PreparedStatement updateTotal = conn.prepareStatement(
                    "UPDATE orders SET total_amount = ? WHERE order_id = ?");
            updateTotal.setDouble(1, total);
            updateTotal.setInt(2, orderId);
            updateTotal.executeUpdate();
            updateTotal.close();

            System.out.printf("[Thread %d] 💰 Update total %.2f for order #%d%n", threadId, total, orderId);

            PreparedStatement statusLog = conn.prepareStatement(
                    "INSERT INTO order_status_history(order_id, status, note) VALUES (?, 'PENDING', 'Order created')");
            statusLog.setInt(1, orderId);
            statusLog.executeUpdate();
            statusLog.close();
        }
    }

    private static int getRandomUserId(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users ORDER BY random() LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int userId = rs.getInt(1);
        rs.close();
        stmt.close();
        return userId;
    }
}
