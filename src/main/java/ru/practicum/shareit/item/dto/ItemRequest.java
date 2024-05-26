package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Вещь.
 */
@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {
    /**
     * Идентификатор вещи.
     */
    private Long id;

    /**
     * Краткое название.
     */
    @NotBlank
    private String name;

    /**
     * Развёрнутое описание.
     */
    @NotBlank
    private String description;

    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    @NotNull
    private Boolean available;
}
