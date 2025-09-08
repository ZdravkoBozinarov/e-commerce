package zdravko.bozinarov.e_commerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zdravko.bozinarov.e_commerce.repository.PasswordResetTokenRepository;
import zdravko.bozinarov.e_commerce.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository users;
    private final PasswordResetTokenRepository tokens;
    private final PasswordEncoder encoder;

    public Optional<String> createLink(String email) {
        var u = users.findByEmail(email);
        if (u.isEmpty()) return Optional.empty();
        var t = UUID.randomUUID().toString().replace("-", "");
        var exp = LocalDateTime.now().plusHours(2);
        tokens.insert(u.get().getUserId(), t, exp);
        return Optional.of("/password/reset?token=" + t);
    }

    public boolean reset(String token, String newPassword) {
        var row = tokens.find(token);
        if (row.isEmpty()) return false;
        if (row.get().used()) return false;
        if (row.get().expiresAt().isBefore(LocalDateTime.now())) return false;
        users.updatePasswordById(row.get().userId(), encoder.encode(newPassword));
        tokens.markUsed(token);
        return true;
    }

}
