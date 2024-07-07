package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.UserRepository;
import ru.gb.electronicsstore.security.SecurityConfig;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    // save new user
    public boolean addUser(UserDTO userDTO) {

        // checking if such user already exists => cannot create the same
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()) return false;

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder(5).encode(userDTO.getPassword()));
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRole(User.ROLE_USER);

        userRepository.save(user);

        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean deleteUserByIdWithOrderConstraint(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Order> orderOptional = orderRepository.findFirstByUser(user);
            // cannot delete user with orders in process
            if (orderOptional.isEmpty()) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }

    public boolean updateUserParameters(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User destinationUser = optionalUser.get();

            // not all the parameters can be updated
            destinationUser.setPhone(user.getPhone());
            destinationUser.setAddress(user.getAddress());

            userRepository.save(destinationUser);

            return true;
        } else {
            return false;
            //throw new IllegalArgumentException("No Order found with id: " + id);
        }
    }
}
