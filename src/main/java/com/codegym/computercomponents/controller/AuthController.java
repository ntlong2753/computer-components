package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.UserRegisterDto;
import com.codegym.computercomponents.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserRegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerProcess(@Valid @ModelAttribute("userDto") UserRegisterDto userDto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            userService.registerNewUserAccount(userDto);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }

        return "redirect:/login?registered";
    }
}
