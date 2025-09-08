package zdravko.bozinarov.e_commerce.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    public void upsert(long userId, long productId, int qty) {
        jdbcTemplate.update("""
            INSERT INTO cart_items(user_id,product_id,qty) VALUES (?,?,?)
            ON CONFLICT(user_id,product_id) DO UPDATE SET qty=cart_items.qty+EXCLUDED.qty
        """, userId, productId, qty);
    }

    public void setQty(long userId, long productId, int qty) {
        jdbcTemplate.update("UPDATE cart_items SET qty=? WHERE user_id=? AND product_id=?", qty, userId, productId);
    }

    public void remove(long userId, long productId) {
        jdbcTemplate.update("DELETE FROM cart_items WHERE user_id=? AND product_id=?", userId, productId);
    }

    public void clear(long userId) {
        jdbcTemplate.update("DELETE FROM cart_items WHERE user_id=?", userId);
    }

    public List<CartRow> list(long userId) {
        return jdbcTemplate.query("""
            SELECT c.product_id,c.qty,p.name,p.price
            FROM cart_items c JOIN products p ON p.id=c.product_id
            WHERE c.user_id=?
        """, (rs,i) -> new CartRow(
                rs.getLong("product_id"),
                rs.getInt("qty"),
                rs.getString("name"),
                rs.getBigDecimal("price")
        ), userId);
    }

    public record CartRow(long productId,int qty,String name,java.math.BigDecimal price){}

    public int count(long userId) {
        Integer c = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(qty),0) FROM cart_items WHERE user_id=?", Integer.class, userId);
        return c == null ? 0 : c;
    }

}
