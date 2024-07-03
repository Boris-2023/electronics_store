package ru.gb.electronicsstore.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    // внедрение зависимости обработчика аутентификации - lombok - конструктор
    private AuthHandler authHandler;

    @Bean //возвращаем кастомный CustomUserDetailsService
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // переопределение фильтра аутентификации, какие ресурсы доступны в зависимости от роли пользователя
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // иначе JS не может обратиться к api без токена (даже если есть права)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/cart/order", "/profile/**").hasAnyRole("USER")
                        .requestMatchers("/**", "/", "/products", "/card", "/cart", "/api/products", "auth/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/auth")
                        .successHandler(authHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean // степень кодировки для пароля в базе
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

}

