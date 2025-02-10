package com.electronics.store.service.impl;

import com.electronics.store.exceptions.ResourcseNotFoundException;
import com.electronics.store.repositories.UserReposiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserReposiory userReposiory;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userReposiory.findByEmail(username).orElseThrow(() -> new ResourcseNotFoundException("User is not found with the given email"));
    }
}
