package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.dto.UserRegisterDto;
import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.model.Role;
import com.codegym.computercomponents.repository.RoleRepository;
import com.codegym.computercomponents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void registerNewUserAccount(UserRegisterDto dto) throws Exception {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("Email đã được sử dụng!");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new Exception("Số điện thoại đã được sử dụng!");
        }

        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Mã hóa BCrypt
        user.setFullName((dto.getFirstName().trim() + " " + dto.getLastName().trim()).trim());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());


        // Gán Role mặc định
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            return roleRepository.save(newRole);
        });

        user.addRole(userRole);

        userRepository.save(user);
    }
}
