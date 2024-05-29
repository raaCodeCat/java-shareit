package ru.practicum.shareit.shareitutils.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для вывода id и name сущности.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
