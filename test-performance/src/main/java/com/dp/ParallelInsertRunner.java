package com.dp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ParallelInsertRunner {

    static class InsertTask extends Thread {
        private final int orderCount;

        public InsertTask(int orderCount) {
            this.orderCount = orderCount;
        }

        @Override
        public void run() {
            String url = "jdbc:postgresql://localhost:5433/performance";
            String user = "postgres";
            String password = "123456";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String call = "CALL insert_bulk_orders(" + orderCount + ")";
                stmt.execute(call);
                System.out.println("Thread " + this.getName() + " inserted " + orderCount + " orders.");

            } catch (Exception e) {
                System.err.println("Thread " + this.getName() + " failed: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int threads = 5;         // 🔥 Số luồng song song
        int ordersPerThread = 10000;  // 📦 Mỗi luồng insert bao nhiêu orders

        for (int i = 0; i < threads; i++) {
            new InsertTask(ordersPerThread).start();
        }
    }
}
