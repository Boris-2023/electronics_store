package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repository;

    public User adduser(User user) {
        repository.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseGet(null);
    }
}
