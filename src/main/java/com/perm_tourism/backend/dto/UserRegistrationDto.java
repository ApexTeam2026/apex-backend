package com.perm_tourism.backend.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Указана некорректная дата рождения")
    private LocalDate birthdayDate;

    @NotNull(message = "Необходимо принять условия политики конфиденциальности")
    @AssertTrue(message = "Вы должны принять условия политики конфиденциальности")
    private Boolean privacyPolicyAccepted;
}
