package com.codegym.computercomponents.config;

import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.model.Role;
import com.codegym.computercomponents.repository.RoleRepository;
import com.codegym.computercomponents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_USER");
            return roleRepository.save(role);
        });

        // Seed Admin User
        if (!userRepository.existsByUsername("admin")) {
            AppUser adminUser = new AppUser();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin@123"));
            adminUser.setFullName("System Administrator");
            adminUser.setEmail("admin@nexgenpc.com");
            adminUser.setPhone("0999999999");
            adminUser.addRole(adminRole);
            
            userRepository.save(adminUser);
            System.out.println("Tài khoản admin mặc định đã được tạo thành công!");
        }
    }
}
