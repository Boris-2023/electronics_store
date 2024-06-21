package ru.gb.electronicsstore.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

// класс - обработчик аунтефикации, в зависимости от роли перенаправляет пользователя
// на страницу корзины (обычный пользователь ROLE_ USER) или на страницу администратора
@Configuration
public class AuthHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
                                        jakarta.servlet.http.HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // получаем роли (у пользователя их м.б. много) в аутентификации и передаем их в сет
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // из response можно извлекать данные по сессии (кто куда заходил и т.д.), 'ROLE_ADMIN' - служ!: сама роль 'ADMIN'
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/cart");
        }
    }
}
