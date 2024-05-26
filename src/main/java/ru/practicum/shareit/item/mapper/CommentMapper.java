package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentView toCommentView(Comment source) {
        return new CommentView(
                source.getId(),
                source.getText(),
                source.getUser().getName(),
                source.getCreated());
    }

    public static List<CommentView> toCommentView(List<Comment> source) {
        return source.stream()
                .map(CommentMapper::toCommentView)
                .collect(Collectors.toList());
    }
}
