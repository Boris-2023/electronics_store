package ru.gb.electronicsstore.aspect;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

// класс аспектов
@Aspect
//@Slf4j
public class LoggingAspect {

    // перед методом с анно @TrackUserAction в консоль выводится его название
    @Before(value = "@annotation(TrackUserAction)")
    public void beforeAdvice(JoinPoint jp) {
    //@AfterReturning(value = "@annotation(TrackUserAction))", returning = "returnedValue")
    //public void beforeAdvice(JoinPoint jp, Object returnedValue) {

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        MethodSignature ms = (MethodSignature) jp.getSignature();

        System.out.println(Arrays.stream(ms.getMethod().getAnnotation(RequestMapping.class).value()).findFirst().get());
        System.out.println(Arrays.stream(ms.getMethod().getAnnotation(RequestMapping.class).method()).findFirst().get());
        System.out.println("Method CALL detected: " + ms.getName() + "()");

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(userName);

        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();

        HttpSession s = (HttpSession) ra.resolveReference(RequestAttributes.REFERENCE_SESSION);
        System.out.println(s.getId());

        String ipAddr = ((ServletRequestAttributes) ra).getRequest().getRemoteAddr();
        System.out.println(ipAddr);

        //log.info("Hello!!!");

    }

    // после того, как метод отработал, в консоль выводится результат его работы
    @AfterReturning(value = "@annotation(TrackUserAction)", returning = "returnedValue")
    public void log(Object returnedValue) {
        String out = returnedValue.toString();
        if (!out.equalsIgnoreCase("redirect/")) out += ".html";
        System.out.println("Return value = " + out);
    }
}

