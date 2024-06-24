package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.dto.UserDTO;
import ru.gb.electronicsstore.service.UserService;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class UserController {

    private UserService service;

    @PostMapping("/auth/registration")
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

        // addNewTaskCounter.increment();

        System.out.println("\nPOST -> " + userDTO + "\n");

        boolean isRegisteredOK = false;
        isRegisteredOK = service.addUser(userDTO);

        // to ensure registration confirmation - only to customers who have just come from auth page
        model.addAttribute("isRegistered", isRegisteredOK);

        if (isRegisteredOK) return "registration";

        return "redirect:/auth?reg_error";
    }


    @GetMapping("/auth")
    public String getLoginPage() {
        return "auth";
    }

}
