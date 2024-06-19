package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repository;

    // save new user
    public boolean addUser(User user) {
        User userFromDB = repository.findByEmail(user.getEmail()).orElseGet(null);

        if (userFromDB != null) {
            return false;
        }

        user.setRole(User.ROLE_USER);
       // user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        repository.save(user);

        return true;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseGet(null);
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}
