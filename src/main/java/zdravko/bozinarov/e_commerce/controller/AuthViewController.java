package zdravko.bozinarov.e_commerce.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zdravko.bozinarov.e_commerce.dto.RegisterForm;
import zdravko.bozinarov.e_commerce.service.AuthService;

@Controller
public class AuthViewController {
    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterForm());
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("form") RegisterForm form,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            ra.addFlashAttribute("form", form);
            return "redirect:/register";
        }
        try {
            authService.register(form.getName(), form.getEmail(), form.getPassword());
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("email", "in_use", "Email already in use");
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            ra.addFlashAttribute("form", form);
            return "redirect:/register";
        }
        ra.addFlashAttribute("registered", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

}
