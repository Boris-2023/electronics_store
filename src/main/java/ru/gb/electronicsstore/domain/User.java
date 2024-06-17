package ru.gb.electronicsstore.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false)
    private String address;

    // used as login
    @Column(length = 50, nullable = false)
    private String email;

    // password
    @Column(name = "pwd",length = 50, nullable = false)
    private String password;

    @Column(name = "user_role", length = 10, nullable = false)
    private String userRole;

}
