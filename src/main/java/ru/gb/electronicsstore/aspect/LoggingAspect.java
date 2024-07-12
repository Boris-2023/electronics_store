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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.http.HttpRequest;
import java.util.Arrays;

// Aspects Class
@Aspect
@Slf4j
public class LoggingAspect {

    private final StringBuilder record = new StringBuilder();

    // before any method with anno @TrackUserAction: retrieve & add data for the record
    @Before(value = "@annotation(TrackUserAction)")
    public void beforeAdvice(JoinPoint jp) {

        MethodSignature ms = (MethodSignature) jp.getSignature();
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        HttpSession s = (HttpSession) ra.resolveReference(RequestAttributes.REFERENCE_SESSION);

        record.append("user: ").append(SecurityContextHolder.getContext().getAuthentication().getName());
        record.append(", ip: ").append(((ServletRequestAttributes) ra).getRequest().getRemoteAddr());
        // logString.append(", session: ").append(s.getId());
        record.append(", address: ").append(Arrays.stream(ms.getMethod().getAnnotation(RequestMapping.class).value()).findFirst()
                .orElseGet(() -> "/"));
        record.append(", request: ").append(Arrays.stream(ms.getMethod().getAnnotation(RequestMapping.class).method()).findFirst()
                .orElseGet(() -> RequestMethod.GET));
        record.append(", class: ").append(ms.getDeclaringType().getSimpleName());
        record.append(", method: ").append(ms.getName());
    }

    // after any method with anno @TrackUserAction: analyse & add method return, request record posting
    @AfterReturning(value = "@annotation(TrackUserAction)", returning = "returnedValue")
    public void log(Object returnedValue) {
        String out;
        String returnClassName = returnedValue.getClass().getSimpleName();

        // web controllers return String
        if (returnClassName.equalsIgnoreCase("string")) {
            out = (String) returnedValue;
            if (!out.contains("redirect:/")) {
                out += ".html";
            }
        // for api controller - response entity is too long to log => cut to response only
        } else {
            out = returnedValue.toString();
            out = out.replace(",[", ">@@@").split("@@@")[0];
        }

        record.append(", return: ").append(out);

        postRecord();
    }

    private void postRecord() {
        log.info(record.toString());
        record.setLength(0);
    }
}

