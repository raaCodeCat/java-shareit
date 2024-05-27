package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.item.model.Comment;
import javax.validation.constraints.NotBlank;

/**
 * Параметры для POST методов {@link Comment}.
 */
@Getter
@Setter
@ToString
public class CommentRequest {
    @NotBlank
    @Length(max = 1024)
    private String text;
}
