package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repository;

    // save new user
    public boolean addUser(UserDTO userDTO) {

        System.out.println("\nSAVE USER: checking if exists '" + userDTO.getEmail() + "'...");

        User user = (repository.findByEmail(userDTO.getEmail())).orElse(null);

        System.out.println(" done:" + user);

        if (user != null) {
            return false;
        }
        System.out.println("NOT null");

        user = new User();
        System.out.println("\nNew user created");

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder(5).encode(userDTO.getPassword()));
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRole(User.ROLE_USER);

        System.out.println("\nSAVE USER on UserService layer\n");

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
