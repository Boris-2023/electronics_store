package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.aspect.TrackUserAction;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.UserService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    UserService userService;

    @TrackUserAction
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @TrackUserAction
    @RequestMapping(value = "/user-delete/{id}", method = RequestMethod.GET)
    public String deleteUserWithOrderConstraints(@PathVariable("id") long id) {
        // cannot delete user with orders in process
        boolean isSucceeded = userService.deleteUserByIdWithOrderConstraint(id);
        if (isSucceeded) {
            return "redirect:/admin/users?deleted";
        }
        return "redirect:/admin/users?delete_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/user-update/{id}", method = RequestMethod.GET)
    public String updateUserForm(@PathVariable("id") long id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "admin/user-update";
        }
        return "redirect:/admin/users?search_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/user-update", method = RequestMethod.POST)
    public String updateUser(User user) {
        userService.updateUserParameters(user.getId(), user);
        return "redirect:/admin/users?updated";
    }

}
