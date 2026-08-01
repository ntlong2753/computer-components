package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.UserRegisterDto;
import com.codegym.computercomponents.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
        if (userService.existsByUsername(userDto.getUsername())) {
            result.rejectValue("username", "error.userDto", "Tên đăng nhập đã tồn tại!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            result.rejectValue("email", "error.userDto", "Email đã được sử dụng!");
        }
        if (userService.existsByPhone(userDto.getPhone())) {
            result.rejectValue("phone", "error.userDto", "Số điện thoại đã được sử dụng!");
        }
        
        if (userDto.getPassword() != null && !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userDto", "Mật khẩu xác nhận không khớp!");
        }

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
