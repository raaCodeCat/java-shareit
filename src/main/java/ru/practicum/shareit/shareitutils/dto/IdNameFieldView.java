package ru.practicum.shareit.shareitutils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для вывода id и name сущности.
 */

@Getter
@Setter
@AllArgsConstructor
public class IdNameFieldView {
    /**
     * Идентификатор.
     */
    private Long id;

    /**
     * Наименование.
     */
    private String name;
}
