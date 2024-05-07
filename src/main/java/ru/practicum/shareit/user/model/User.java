package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Пользователь.
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class User {
    /**
     * Идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Адрес электронной почты.
     */
    private String email;
}
