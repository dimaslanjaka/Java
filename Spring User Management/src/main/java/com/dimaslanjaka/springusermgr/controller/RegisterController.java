package com.dimaslanjaka.springusermgr.controller;

import com.dimaslanjaka.springusermgr.entities.User;
import com.dimaslanjaka.springusermgr.entities.UserDto;
import com.dimaslanjaka.springusermgr.entities.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        } else {

            return "register";
        }
    }

    @PostMapping("/register/save")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "field.user.exist",
                    "There is already an account registered with the same email");
        }

        if (!userDto.getPassword().isEmpty()) {
            if (userDto.getPassword().length() < 7) {
                result.rejectValue("password", "field.min.length", "Password should have at least 7 characters");
            }
        } else {
            result.rejectValue("password", "field.min.length", "Password should not be empty.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success=true";
    }
}
