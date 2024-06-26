package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentRequest {
    @NotBlank
    @Length(max = 1024)
    private String text;
}
