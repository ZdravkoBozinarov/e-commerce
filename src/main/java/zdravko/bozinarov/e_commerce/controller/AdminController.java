package zdravko.bozinarov.e_commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zdravko.bozinarov.e_commerce.service.AdminService;

@Controller
public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) { this.service = service; }

    @GetMapping("/admin/statistics")
    public String statistics(@RequestParam(defaultValue = "5") int top,
                             @RequestParam(defaultValue = "5") int low,
                             Model model) {
        model.addAttribute("topSellers", service.getTopSellers(top));
        model.addAttribute("monthlyRevenue", service.getMonthlyRevenue());
        model.addAttribute("lowStock", service.getLowStock(low));
        return "statistics";
    }
}
