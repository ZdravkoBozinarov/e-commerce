package zdravko.bozinarov.e_commerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import zdravko.bozinarov.e_commerce.repository.ProductRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductRepository products;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", products.findFeatured(6));
        return "index";
    }
}
