package zdravko.bozinarov.e_commerce.repository;

import zdravko.bozinarov.e_commerce.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepository {
    private final JdbcTemplate jdbc;

    public AdminRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<ProductSalesDTO> topSellers(int limit) {
        String sql = """
            SELECT p.name, SUM(oi.quantity) total_sold
            FROM order_items oi
            JOIN products p ON p.product_id = oi.product_id
            GROUP BY p.name
            ORDER BY total_sold DESC
            LIMIT ?
        """;
        return jdbc.query(sql, (rs, n) -> new ProductSalesDTO(
                rs.getString("name"),
                rs.getLong("total_sold")
        ), limit);
    }

    public List<MonthlyRevenueDTO> monthlyRevenue() {
        String sql = """
            SELECT to_char(date_trunc('month', o.order_date), 'YYYY-MM') AS month,
                   SUM(oi.price * oi.quantity) AS revenue
            FROM orders o
            JOIN order_items oi ON oi.order_id = o.order_id
            GROUP BY month
            ORDER BY month
        """;
        return jdbc.query(sql, (rs, n) -> new MonthlyRevenueDTO(
                rs.getString("month"),
                rs.getBigDecimal("revenue")
        ));
    }

    public List<LowStockDTO> lowStock(int threshold) {
        String sql = """
            SELECT p.name AS product, s.size_name AS size, ps.stock
            FROM product_sizes ps
            JOIN products p ON p.product_id = ps.product_id
            JOIN sizes s ON s.size_id = ps.size_id
            WHERE ps.stock < ?
            ORDER BY ps.stock ASC, p.name, s.size_name
        """;
        return jdbc.query(sql, (rs, n) -> new LowStockDTO(
                rs.getString("product"),
                rs.getString("size"),
                rs.getInt("stock")
        ), threshold);
    }
}
