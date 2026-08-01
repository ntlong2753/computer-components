package com.codegym.computercomponents.controller.api;

import com.codegym.computercomponents.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final UserService userService;

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam String phone) {
        boolean exists = userService.existsByPhone(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @org.springframework.web.bind.annotation.PostMapping("/ajax-register")
    public ResponseEntity<Map<String, String>> ajaxRegister(@org.springframework.web.bind.annotation.RequestBody com.codegym.computercomponents.dto.UserRegisterDto userDto) {
        Map<String, String> response = new HashMap<>();
        try {
            if (userService.existsByUsername(userDto.getUsername())) {
                response.put("error", "Tên đăng nhập đã tồn tại!");
                return ResponseEntity.badRequest().body(response);
            }
            if (userService.existsByEmail(userDto.getEmail())) {
                response.put("error", "Email đã được sử dụng!");
                return ResponseEntity.badRequest().body(response);
            }
            if (userService.existsByPhone(userDto.getPhone())) {
                response.put("error", "Số điện thoại đã được sử dụng!");
                return ResponseEntity.badRequest().body(response);
            }
            userService.registerNewUserAccount(userDto);
            response.put("success", "Đăng ký thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
