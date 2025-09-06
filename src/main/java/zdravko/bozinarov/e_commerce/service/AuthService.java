package zdravko.bozinarov.e_commerce.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zdravko.bozinarov.e_commerce.model.User;
import zdravko.bozinarov.e_commerce.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public User register(String name, String email, String rawPassword) {
        if (users.existsByEmail(email)) throw new IllegalStateException("email_in_use");
        User u = new User(null, name, email, encoder.encode(rawPassword), "USER");
        return users.save(u);
    }
}