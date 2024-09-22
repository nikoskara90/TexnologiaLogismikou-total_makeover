package com.icsd19080_icsd19235.controller;

import com.icsd19080_icsd19235.model.User;
import com.icsd19080_icsd19235.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        System.out.println("Login page requested"); //DEBUG
        return "login";  
    }

 @PostMapping("/login")
    public String login(@PathVariable String username, 
                        @PathVariable String password, 
                        Model model) {
        System.out.println("Login attempt: Username = " + username + ", Password = " + password); //DEBUG

        User user = new User(username, password, null);
        User authenticatedUser = userService.login(user);

        if (authenticatedUser != null) {
            System.out.println("Login successful for user: " + authenticatedUser.getUsername()); //DEBUG
            model.addAttribute("fullname", authenticatedUser.getFullName());
            return "welcome"; 
        } else {
            System.out.println("Login failed for user: " + username); //DEBUG
            model.addAttribute("error", "Invalid username or password");
            return "login"; 
        }
    }

    @GetMapping("/welcome")
    public String welcomePage(Model model, Authentication authentication) {
        String fullName = authentication.getName();
        model.addAttribute("fullname", fullName);
        return "welcome";  
    }

}
