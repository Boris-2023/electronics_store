package ru.gb.electronicsstore.aspect;

import java.lang.annotation.*;

// анно с областью видимости в RUNTIME (работа приложения), для методов
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TrackUserAction {
}
