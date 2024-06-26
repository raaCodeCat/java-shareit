package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Вещь.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemCreateUpdateDto {
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

    /**
     * Идентификатор запроса.
     */
    private Long requestId;
}
