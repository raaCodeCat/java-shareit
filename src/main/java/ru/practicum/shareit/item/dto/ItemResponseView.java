package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для описания вещи в отклике на запрос.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemResponseView {
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
     * Идентификатор запроса.
     */
    private Long requestId;
}
