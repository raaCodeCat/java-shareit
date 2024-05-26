package ru.practicum.shareit.shareitutils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для вывода id сущности.
 */

@Getter
@Setter
@AllArgsConstructor
public class IdFieldView {
    /**
     * Идентификатор.
     */
    private Long id;
}
