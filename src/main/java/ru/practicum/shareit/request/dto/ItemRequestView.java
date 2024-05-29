package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequestView {
    /**
     * Идентификатор запроса.
     */
    private Long id;

    /**
     * Описание запроса.
     */
    private String description;

    /**
     * Дата и время создания запроса.
     */
    private LocalDateTime created;
}
