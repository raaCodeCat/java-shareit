package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;
import java.time.LocalDateTime;

/**
 * DTO для ответа {@link Comment}.
 */
@Getter
@Setter
@AllArgsConstructor
public class CommentView {
    /**
     * Идентификатор отзыва.
     */
    private Long id;

    /**
     * Текст отзыва.
     */
    private String text;

    /**
     * Автор отзыва.
     */
    private String authorName;

    /**
     * Дата создания отзыва.
     */
    private LocalDateTime created;
}
