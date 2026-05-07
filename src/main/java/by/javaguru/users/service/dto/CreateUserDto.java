package by.javaguru.users.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя не может быть короче 2 и длиннее 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 50, message = "Фамилия не может быть короче 2 и длиннее 50 символов")
    private String lastName;

    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть короче 8 и длиннее 30 символов")
    private String password;

}
