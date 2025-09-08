package zdravko.bozinarov.e_commerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import zdravko.bozinarov.e_commerce.repository.CartRepository;
import zdravko.bozinarov.e_commerce.repository.UserRepository;
import zdravko.bozinarov.e_commerce.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService service;
    private final CartRepository repo;
    private final UserRepository users;

    @GetMapping
    public String view(@AuthenticationPrincipal(expression = "username") String email, Model model) {
        long id = users.getIdByEmail(email);
        model.addAttribute("items", repo.list(id));
        return "cart/index";
    }

    @PostMapping("/add")
    public String add(@AuthenticationPrincipal(expression = "username") String email,
                      @RequestParam long productId, @RequestParam int qty) {
        long id = users.getIdByEmail(email);
        service.add(id, productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@AuthenticationPrincipal(expression = "username") String email,
                         @RequestParam long productId) {
        long id = users.getIdByEmail(email);
        service.remove(id, productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(@AuthenticationPrincipal(expression = "username") String email) {
        long id = users.getIdByEmail(email);
        service.clear(id);
        return "redirect:/cart";
    }
}
