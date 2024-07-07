package ru.gb.electronicsstore.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.UserRepository;
import ru.gb.electronicsstore.service.UserService;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceModuleTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService service;


    // test if new user is added correctly
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
        service.addUser(dto);

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
        service.updateUserParameters(1L, userUpdated);

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
        service.deleteUserByIdWithOrderConstraint(1L);
        service.deleteUserByIdWithOrderConstraint(2L);

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
