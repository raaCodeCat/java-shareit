package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryIT {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item1;
    private Item item2;
    private Comment comment;


    @BeforeEach
    void setUp() {
        User owner = new User(null, "owner", "owner@email.com");
        userRepository.save(owner);
        User user = new User(null, "user", "user@email.com");
        userRepository.save(user);
        item1 = new Item(null, "name_1", "description_1", true, owner, null);
        itemRepository.save(item1);
        item2 = new Item(null, "name_2", "description_2", true, owner, null);
        itemRepository.save(item2);
        comment = new Comment(null, "text", item1, user, LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Test
    void findCommentsByItemIds_whenItemHasComments_thenReturnNotEmptyCollection() {
        List<Comment> comments = commentRepository.findCommentsByItemIds(List.of(item1.getId()));

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comment, comments.get(0));
    }

    @Test
    void findCommentsByItemIds_whenItemDoesNotHasComments_thenReturnEmptyCollection() {
        List<Comment> comments = commentRepository.findCommentsByItemIds(List.of(item2.getId()));

        assertNotNull(comments);
        assertEquals(0, comments.size());
    }

    @Test
    void findCommentsByItemIds_whenItemIdIsNotValid_thenReturnEmptyCollection() {
        List<Comment> comments = commentRepository.findCommentsByItemIds(List.of(100L));

        assertNotNull(comments);
        assertEquals(0, comments.size());
    }
}