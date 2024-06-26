package ru.gb.electronicsstore.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

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
    @Column(length = 50, nullable = false, unique = true)
    private String email;

    // password
    @Column(name = "pwd", nullable = false)
    private String password;

    @Column(name = "user_role", length = 100, nullable = false)
    private String role;

}
