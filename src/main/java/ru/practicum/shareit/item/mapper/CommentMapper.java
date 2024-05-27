package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentView toCommentView(Comment source) {
        return new CommentView(
                source.getId(),
                source.getText(),
                source.getUser().getName(),
                source.getCreated());
    }
}
