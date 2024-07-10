package ru.gb.electronicsstore.service.implementation;

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
import ru.gb.electronicsstore.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    // save new user
    public boolean addUser(UserDTO userDTO) {

        try {
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

        } catch (Exception e) {
            System.out.println("Failed to connect database in addUser(): " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getAllUsers(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<User> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getUserById(): " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getUserByEmail(): " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUserByIdWithOrderConstraint(Long id) {
        try {
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
        } catch (Exception e) {
            System.out.println("Failed to connect database in deleteUserByIdWithOrderConstraint(): " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserParameters(Long id, User user) {
        try {
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
        } catch (Exception e) {
            System.out.println("Failed to connect database in updateUserParameters(): " + e.getMessage());
            return false;
        }
    }
}
