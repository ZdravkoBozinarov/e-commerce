package zdravko.bozinarov.e_commerce.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import zdravko.bozinarov.e_commerce.repository.CartRepository;
import zdravko.bozinarov.e_commerce.repository.UserRepository;

import java.security.Principal;

@ControllerAdvice
@Controller
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final UserRepository users;
    private final CartRepository cart;

    @ModelAttribute("cartCount")
    public Integer cartCount(Principal principal) {
        if (principal == null) return 0;
        long id = users.getIdByEmail(principal.getName());
        return cart.count(id);
    }
}
