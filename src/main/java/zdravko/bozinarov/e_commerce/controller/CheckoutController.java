package zdravko.bozinarov.e_commerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import zdravko.bozinarov.e_commerce.repository.CartRepository;
import zdravko.bozinarov.e_commerce.repository.UserRepository;
import zdravko.bozinarov.e_commerce.service.OrderService;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orders;
    private final CartRepository cart;
    private final UserRepository users;

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal(expression = "username") String email) {
        long id = users.getIdByEmail(email);
        orders.createFromCart(id);
        cart.clear(id);
        return "redirect:/orders";
    }
}
