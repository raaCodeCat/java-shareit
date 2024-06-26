package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemResponseView;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для ответа на запрос пользователя, со списком откликов.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequestUserView {
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

    /**
     * Список откликов.
     */
    List<ItemResponseView> items;
}
