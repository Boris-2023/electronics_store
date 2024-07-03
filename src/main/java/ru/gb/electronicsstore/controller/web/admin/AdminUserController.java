package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.UserService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/user-delete/{id}")
    public String deleteUserWithOrderConstraints(@PathVariable("id") long id) {
        // cannot delete user with orders in process
        boolean isSucceeded = userService.deleteUserByIdWithOrderConstraint(id);
        if (isSucceeded) {
            return "redirect:/admin/users?deleted";
        }
        return "redirect:/admin/users?delete_failed";
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") long id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "admin/user-update";
        }
        return "redirect:/admin/users?search_failed";
    }

    @PostMapping("/user-update")
    public String updateUser(User user) {
        userService.updateUserParameters(user.getId(), user);
        return "redirect:/admin/users?updated";
    }

}
