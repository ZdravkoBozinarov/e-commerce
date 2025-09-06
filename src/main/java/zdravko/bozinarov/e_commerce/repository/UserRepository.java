package zdravko.bozinarov.e_commerce.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zdravko.bozinarov.e_commerce.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> mapper = (rs, rn) -> new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("role")
    );

    public Optional<User> findByEmail(String email) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", mapper, email);
        return list.stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        Boolean exists = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE email=?)", Boolean.class, email);
        return Boolean.TRUE.equals(exists);
    }

    public User save(User user) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?) RETURNING user_id",
                Integer.class,
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole()
        );
        user.setUserId(id);
        return user;
    }
}