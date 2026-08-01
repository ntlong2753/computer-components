package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.codegym.computercomponents.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        Collection<GrantedAuthority> authorities = appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        String avatarUrl = appUser.getUserAvatar() != null ? appUser.getUserAvatar().getImageUrl() : null;

        return new CustomUserDetails(
                appUser.getUsername(), 
                appUser.getPassword(), 
                authorities, 
                appUser.getFullName(), 
                avatarUrl
        );
    }
}
