package com.codegym.computercomponents.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {

    private final String fullName;
    private final String avatarUrl;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String fullName, String avatarUrl) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
