package zdravko.bozinarov.e_commerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zdravko.bozinarov.e_commerce.service.PasswordResetService;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordResetService service;

    @GetMapping("/forgot")
    public String forgotForm() {
        return "password/forgot";
    }

    @PostMapping("/forgot")
    public String forgotSubmit(@RequestParam String email, Model model){
        var link = service.createLink(email);
        model.addAttribute("sent", true);
        link.ifPresent(l -> model.addAttribute("link", l));
        return "password/forgot";
    }

    @GetMapping("/reset")
    public String resetForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "password/reset";
    }

    @PostMapping("/reset")
    public String resetSubmit(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirm,
                              RedirectAttributes redirectAttributes) {
        if (!password.equals(confirm)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/password/reset?token=" + token;
        }

        var ok = service.reset(token, password);
        if (!ok) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token");
            return "redirect:/password/reset?token=" + token;
        } else {
            redirectAttributes.addFlashAttribute("success", "Password reset successfully. You can now log in.");
            return "redirect:/login";
        }
    }
}
