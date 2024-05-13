package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

/**
 * Вещь.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Item {
    /**
     * Идентификатор вещи.
     */
    private Long id;

    /**
     * Краткое название.
     */
    private String name;

    /**
     * Развёрнутое описание.
     */
    private String description;

    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    private Boolean available;

    /**
     * Владелец вещи.
     */
    private User owner;
}
