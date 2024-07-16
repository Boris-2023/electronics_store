package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.aspect.TrackUserAction;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.service.UserService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    // adding new user based on dto received
    @TrackUserAction
    @RequestMapping(value = "/auth/registration", method = RequestMethod.POST)
    public String addUser(
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "address") String address,
            Model model
    ) {
        UserDTO userDTO = new UserDTO();

        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhone(phone);
        userDTO.setAddress(address);

        boolean isRegisteredOK;
        isRegisteredOK = userService.addUser(userDTO);

        // to ensure registration confirmation - only to customers who have just come from auth page
        model.addAttribute("isRegistered", isRegisteredOK);

        if (isRegisteredOK) return "registration";

        return "redirect:/auth?reg_error";
    }

    @TrackUserAction
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String getLoginPage() {
        return "auth";
    }

    @TrackUserAction
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String displayUserData(Model model) {

        // gets a name of user from security context
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userName).orElse(null);

        model.addAttribute("user", user);

        return "user/profile";
    }

    @TrackUserAction
    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String editUserForm(Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(userName);

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user/edit";
        }
        return "redirect:/profile?search_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String updateUser(User user) {
        userService.updateUserParameters(user.getId(), user);
        return "redirect:/profile";
    }
}
