package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;
import java.time.LocalDateTime;

/**
 * DTO для ответа {@link Comment}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
