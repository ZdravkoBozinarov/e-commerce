package zdravko.bozinarov.e_commerce.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    public void insert(long userId, String token, LocalDateTime expiresAt) {
        jdbcTemplate.update("INSERT INTO password_reset_tokens(user_id, token, expires_at) VALUES (?,?,?)",
                userId, token, Timestamp.valueOf(expiresAt));
    }

    public Optional<TokenRow> find(String token) {
        var list = jdbcTemplate.query(
                "SELECT id,user_id,token,expires_at,used FROM password_reset_tokens WHERE token=?",
                (rs, i) -> new TokenRow(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("token"),
                        rs.getTimestamp("expires_at").toLocalDateTime(),
                        rs.getBoolean("used")
                ), token);
        return list.stream().findFirst();
    }

    public void markUsed(String token) {
        jdbcTemplate.update("UPDATE password_reset_tokens SET used=true WHERE token=?", token);
    }

    public record TokenRow(long id, long userId, String token, LocalDateTime expiresAt, boolean used) {}
}
