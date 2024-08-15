package ru.gb.electronicsstore.service;


import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    boolean addUser(UserDTO userDTO);

    boolean deleteUserByIdWithOrderConstraint(Long id);

    boolean updateUserParameters(Long id, User user);
}
