package technikal.task.fishmarket.controllers;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import technikal.task.fishmarket.models.dto.UserDto;
import technikal.task.fishmarket.models.entity.Role;
import technikal.task.fishmarket.services.UserService;

@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService users;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "login";
    }

    @PostMapping("/createUser")
    public String registerUser(@ModelAttribute("userDto") @Valid UserDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return "createUser";
        }
        users.save(dto, Role.USER);
        return "redirect:/login";
    }

    @GetMapping("/createUser")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "createUser";
    }
}