package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    /**
     * Идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    @NotBlank
    private String name;

    /**
     * Адрес электронной почты.
     */
    @NotBlank
    @Email
    private String email;
}
