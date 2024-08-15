package ru.gb.electronicsstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.gb.electronicsstore.aspect.LoggingAspect;

@Configuration
@ComponentScan(basePackages = "ru.gb.electronicsstore") // where are objects to scan
@EnableAspectJAutoProxy // turn on the aspects
public class ProjectConfiguration {
    @Bean
    public LoggingAspect aspectMethod(){
        return new LoggingAspect();
    }
}