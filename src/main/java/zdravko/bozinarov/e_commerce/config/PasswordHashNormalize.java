package zdravko.bozinarov.e_commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PasswordHashNormalize {
    @Bean
    ApplicationRunner normalize(@Value("${PASSWORD_HASH_NORMALIZE_ON_START:false}") boolean on, JdbcTemplate jdbc) {
        return args -> {
            if (on) {
                jdbc.update("UPDATE users SET password_hash = '{bcrypt}' || password_hash WHERE password_hash NOT LIKE '{%' AND password_hash LIKE '$2%';");
            }
        };
    }
}
