package com.codegym.computercomponents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import com.codegym.computercomponents.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**",
                                "/uploads/**", "/api/auth/**", "/api/cart/**", "/cart", "/product/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                                response.setStatus(401);
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter()
                                        .write("{\"error\":\"Tên đăng nhập hoặc mật khẩu không chính xác\"}");
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                // Tắt CSRF để việc gọi AJAX đơn giản hơn (trong dự án thực tế, hãy xử lý CSRF
                // token đúng cách)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
