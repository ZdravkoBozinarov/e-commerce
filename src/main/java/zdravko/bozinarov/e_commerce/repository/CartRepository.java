package zdravko.bozinarov.e_commerce.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Item> list(long userId) {
        return jdbcTemplate.query("""
            SELECT c.product_id, p.name, p.price, c.qty, COALESCE(p.image_url,'') AS image_url
            FROM cart_items c
            JOIN products p ON p.product_id = c.product_id
            WHERE c.user_id = ?
            ORDER BY p.product_id DESC
        """, (rs, i) -> new Item(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getInt("qty"),
                rs.getString("image_url")
        ), userId);
    }

    public void upsert(long userId, long productId, int qty) {
        jdbcTemplate.update("""
            INSERT INTO cart_items(user_id, product_id, qty)
            VALUES (?,?,?)
            ON CONFLICT (user_id, product_id) DO UPDATE SET qty = cart_items.qty + EXCLUDED.qty
        """, userId, productId, qty);
    }

    public void remove(long userId, long productId) {
        jdbcTemplate.update("DELETE FROM cart_items WHERE user_id=? AND product_id=?", userId, productId);
    }

    public void clear(long userId) {
        jdbcTemplate.update("DELETE FROM cart_items WHERE user_id=?", userId);
    }

    public int count(long userId) {
        Integer c = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(qty),0) FROM cart_items WHERE user_id=?", Integer.class, userId);
        return c == null ? 0 : c;
    }

    public record Item(int productId, String name, java.math.BigDecimal price, int qty, String imageUrl) {}
}
