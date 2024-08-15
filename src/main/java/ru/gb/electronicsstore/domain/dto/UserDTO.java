package ru.gb.electronicsstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
}
