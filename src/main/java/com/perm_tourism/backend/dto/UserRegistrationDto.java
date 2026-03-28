package com.perm_tourism.backend.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UserRegistrationDto {
    @Pattern(regexp= "^[a-zA-Zа-яА-Я\\s-]+$", message = "Имя пользователя может содержать только буквы, пробелы и дефисы")
    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 2,max = 100, message = "Имя пользователя должно быть от 2 до 100 символов")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "Email должен соответствовать стандартному формату")
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Пароль должен содержать минимум одну цифру, одну заглавную букву, " +
            "одну строчную, один спецсимвол и не содержать пробелов")
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max= 100, message = "Пароль должен содержать от 8 до 100 символов")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Указана некорректная дата рождения")
    private LocalDate birthdayDate;

    @NotNull(message = "Необходимо принять условия политики конфиденциальности")
    @AssertTrue(message = "Вы должны принять условия политики конфиденциальности")
    private Boolean privacyPolicyAccepted;
}
