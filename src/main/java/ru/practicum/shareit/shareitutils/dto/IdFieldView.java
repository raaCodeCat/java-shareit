package ru.practicum.shareit.shareitutils.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для вывода id сущности.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class IdFieldView {
    /**
     * Идентификатор.
     */
    private Long id;
}
