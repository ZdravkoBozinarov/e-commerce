package zdravko.bozinarov.e_commerce.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zdravko.bozinarov.e_commerce.service.AuthService;

@Data
class RegisterRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;
}

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(auth.register(req.getName(), req.getEmail(), req.getPassword()));
    }
}
