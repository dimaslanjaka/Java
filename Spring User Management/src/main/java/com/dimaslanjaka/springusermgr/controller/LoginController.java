package com.dimaslanjaka.springusermgr.controller;

import java.util.Locale;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dimaslanjaka.springusermgr.CustomPassword;
import com.dimaslanjaka.springusermgr.entities.User;
import com.dimaslanjaka.springusermgr.entities.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    private CustomPassword passwordEncoder;
    private AuthenticationManager authenticationManager;

    public LoginController(UserService userService, CustomPassword passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginForm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @RequestMapping(value = "/login/{tokenId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String loginToken(@PathVariable String tokenId, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Optional<User> find = Optional.ofNullable(userService.findUserByToken(tokenId));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (find.isPresent()) {
            User user = find.get();
            String password = passwordEncoder.decode(user.getPassword());
            String email = user.getEmail();
            jsonObject.put("email", user.getEmail());
            try {
                UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(email,
                        password);
                Authentication authenticatedUser = authenticationManager.authenticate(loginToken);
                securityContext.setAuthentication(authenticatedUser);
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            } catch (Exception e) {
                System.err.println("Error while login");
            }
        }
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            jsonObject.put("is login", authentication.isAuthenticated());
            jsonObject.put("authorities", authentication.getAuthorities());
            jsonObject.put("authorities has admin", authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().toLowerCase(Locale.ROOT).contains("admin")));
        }
        return jsonObject.toString();
    }
}
