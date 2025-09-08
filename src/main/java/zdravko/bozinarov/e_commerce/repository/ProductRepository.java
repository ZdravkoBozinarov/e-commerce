package zdravko.bozinarov.e_commerce.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<ProductRow> findFeatured(int limit) {
        return jdbcTemplate.query(
                "SELECT product_id,name,description,price,image_url FROM products ORDER BY product_id DESC LIMIT ?",
                (rs,i) -> new ProductRow(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("image_url")
                ),
                limit
        );
    }

    public int countAll() {
        Integer c = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
        return c == null ? 0 : c;
    }

    public void ensureOptionalColumns() {
        jdbcTemplate.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url TEXT");
        jdbcTemplate.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS stock INT NOT NULL DEFAULT 100");
    }

    public void insert(String name, String description, BigDecimal price, String imageUrl, int stock) {
        jdbcTemplate.update(
                "INSERT INTO products(name, description, price, image_url, stock) VALUES (?,?,?,?,?)",
                name, description, price, imageUrl, stock
        );
    }

    public record ProductRow(int productId, String name, String description, java.math.BigDecimal price, String imageUrl) {}
}
