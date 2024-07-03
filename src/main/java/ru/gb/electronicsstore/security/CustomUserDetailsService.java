package ru.gb.electronicsstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.UserService;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService service;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = service.getUserByEmail(username);

        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + "Такой пользователь не найден!"));
    }
}
