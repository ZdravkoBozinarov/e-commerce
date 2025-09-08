package zdravko.bozinarov.e_commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import zdravko.bozinarov.e_commerce.model.User;
import zdravko.bozinarov.e_commerce.repository.UserRepository;

@Configuration
public class AdminSeedConfig {
    @Bean
    ApplicationRunner adminSeed(UserRepository users, PasswordEncoder enc,
                                @Value("${ADMIN_EMAIL:admin@store.test}") String email,
                                @Value("${ADMIN_PASSWORD:}") String pass,
                                @Value("${ADMIN_RESET:false}") boolean reset) {
        return args -> {
            var existing = users.findByEmail(email);
            if (existing.isEmpty() && !pass.isBlank()) {
                users.save(new User(null, "Administrator", email, enc.encode(pass), "ADMIN"));
            } else if (existing.isPresent() && reset && !pass.isBlank()) {
                users.updateCredentialsByEmail(email, enc.encode(pass), "ADMIN");
            }
        };
    }
}
