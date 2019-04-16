package com.mdud.sternumauth.security;

import com.mdud.sternumauth.user.User;
import com.mdud.sternumauth.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findDistinctByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user " + username + " not found"));

        Set<GrantedAuthority> authoritySet = new HashSet<>();
        user.getAuthoritySet().forEach(userAuthority ->
                authoritySet.add(new SimpleGrantedAuthority(userAuthority.getAuthorityType().toString())));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getActive())
                .authorities(authoritySet)
                .build();
    }
}
