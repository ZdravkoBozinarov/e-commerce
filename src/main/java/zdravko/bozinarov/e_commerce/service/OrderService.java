package zdravko.bozinarov.e_commerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final JdbcTemplate jdbc;

    public void createFromCart(long userId) {
        Long orderId = jdbc.queryForObject("INSERT INTO orders(user_id,created_at) VALUES (?,now()) RETURNING id", Long.class, userId);
        jdbc.update("""
            INSERT INTO order_items(order_id, product_id, quantity, unit_price)
            SELECT ?, c.product_id, c.qty, p.price
            FROM cart_items c JOIN products p ON p.id=c.product_id
            WHERE c.user_id=?
        """, orderId, userId);
    }
}
