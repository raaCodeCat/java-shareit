package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.model.Comment;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toCommentView() {
        Comment comment = new Comment();
        CommentView commentView = new CommentView();

        CommentView actual = CommentMapper.toCommentView(comment);

        assertNotNull(actual);
        assertEquals(commentView, actual);
    }
}