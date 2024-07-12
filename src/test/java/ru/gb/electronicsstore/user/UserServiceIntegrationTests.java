package ru.gb.electronicsstore.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.UserRepository;
import ru.gb.electronicsstore.service.implementation.UserServiceImpl;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceIntegrationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceImpl userService;


    // test new user adding
    @Test
    public void userAddTest() {

        // precondition
        UserDTO dto = new UserDTO();
        dto.setFirstName("First");
        dto.setLastName("Last");
        dto.setEmail("email");
        dto.setPassword("password");
        dto.setPhone("12345");
        dto.setAddress("address");

        User user = createUserFromDTO(dto);

        // action
        userService.addUser(dto);

        // result check
        verify(userRepository).save(user);
    }

    // test user data update
    @Test
    public void userUpdateParametersTest() {

        // precondition
        UserDTO dto = new UserDTO();
        dto.setFirstName("First");
        dto.setLastName("Last");
        dto.setEmail("email");
        dto.setPassword("password");
        dto.setPhone("12345");
        dto.setAddress("address");

        User user = createUserFromDTO(dto);

        User userUpdated = createUserFromDTO(dto);
        userUpdated.setPhone("000");
        userUpdated.setAddress("new address");

        given(userRepository.findById(1L)).willReturn(Optional.of(user));// describe repo's behaviour

        // action
        userService.updateUserParameters(1L, userUpdated);

        // performance check
        verify(userRepository).findById(1L);
        verify(userRepository).save(userUpdated);
    }

    // test deletion of user: can delete user without active orders, but cannot delete with such
    @Test
    public void deleteUserByIdWithOrderConstraintTest() {

        // precondition
        UserDTO dto = new UserDTO();
        dto.setFirstName("First");
        dto.setLastName("Last");
        dto.setEmail("email");
        dto.setPassword("password");
        dto.setPhone("12345");
        dto.setAddress("address");

        User userNoOrder = createUserFromDTO(dto);
        userNoOrder.setFirstName("FirstNoOrder"); // users must not be equal
        User userWithOrder = createUserFromDTO(dto);

        // stubbing - describe repo's behaviour
        given(userRepository.findById(1L)).willReturn(Optional.of(userNoOrder));
        given(userRepository.findById(2L)).willReturn(Optional.of(userWithOrder));
        given(orderRepository.findFirstByUser(userNoOrder)).willReturn(Optional.empty());
        given(orderRepository.findFirstByUser(userWithOrder)).willReturn(Optional.of(new Order()));

        // action
        userService.deleteUserByIdWithOrderConstraint(1L);
        userService.deleteUserByIdWithOrderConstraint(2L);

        // performance check
        verify(userRepository).delete(userNoOrder);
        verify(userRepository, never()).delete(userWithOrder);

    }

    private static User createUserFromDTO(UserDTO dto) {

        User user = new User();
        user.setId(1L);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setRole(User.ROLE_USER);

        return user;
    }
}
