package webappusers.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import webappusers.User;
import webappusers.security.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepo;

    public RegistrationController(
            UserRepository userRepository, PasswordEncoder passwordEncoder){

        this.passwordEncoder = passwordEncoder;

        this.userRepo = userRepository;
    }

@GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {

        User user = userRepo.findByUsername(form.getUsername());

        if(user != null) return "registration";

        userRepo.save(form.toUser(passwordEncoder));

        return "redirect:/login";
    }

}
