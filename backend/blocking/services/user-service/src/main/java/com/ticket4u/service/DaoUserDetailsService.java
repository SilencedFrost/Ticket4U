package com.ticket4u.service;

import com.ticket4u.entity.CustomUserDetails;
import com.ticket4u.entity.User;
import com.ticket4u.repository.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

public class DaoUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Nonnull
    public UserDetails loadUserByUsername(@Nonnull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        String authority = user.getRole().getRoleName();
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));
        return new CustomUserDetails(
                user.getEmail(),
                user.getPasswordHash(),
                authorities,
                user.getId(),
                user.getRole().getId(),
                user.getUsername()
        );
    }
}
