package com.dimaslanjaka.springusermgr.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Dashboard {
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // check anonymous
            boolean isAnon = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().toLowerCase().contains("anonymous"));
            // check if is not anonymous and not authenticated
            if (!authentication.isAuthenticated() || isAnon) {
                return "redirect:/login";
            }
        }
        return "dashboard";
    }
}
