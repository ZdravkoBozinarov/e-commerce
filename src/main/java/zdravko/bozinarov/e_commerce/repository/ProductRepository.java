package zdravko.bozinarov.e_commerce.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public record ProductRow(int productId, String name, String description, java.math.BigDecimal price, String imageUrl) {}
}
