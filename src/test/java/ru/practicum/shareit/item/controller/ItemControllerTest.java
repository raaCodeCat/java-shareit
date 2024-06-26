package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void getUserItems_whenInvoked_thenResponseStatusOkWithItemViewCollectionInBody() {
        final Long userId = 1L;
        final List<ItemView> expectedItemView = List.of(new ItemView());
        when(itemService.getUserItems(userId))
                .thenReturn(expectedItemView);

        final ResponseEntity<List<ItemView>> response = itemController.getUserItems(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemView, response.getBody());
    }

    @Test
    void getItemById_whenInvoked_thenResponseStatusOkWithItemViewInBody() {
        final Long userId = 1L;
        final Long itemId = 1L;
        ItemView expectedItemView = new ItemView();
        when(itemService.getItemById(userId, itemId))
                .thenReturn(expectedItemView);

        final ResponseEntity<ItemView> response = itemController.getItemById(userId, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemView, response.getBody());
    }

    @Test
    void searchItem_whenInvoked_thenResponseStatusOkWithItemViewCollectionInBody() {
        final Long userId = 1L;
        final String searchText = "search";
        final List<ItemView> expectedItemView = List.of(new ItemView());
        when(itemService.searchItems(searchText))
                .thenReturn(expectedItemView);

        final ResponseEntity<List<ItemView>> response = itemController.searchItem(userId, searchText);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemView, response.getBody());
    }

    @Test
    void addItem_whenInvoked_thenResponseStatusOkWithItemViewInBody() {
        final Long userId = 1L;
        final ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        final ItemView expectedItemView = new ItemView();

        when(itemService.create(userId, itemCreateUpdateDto))
                .thenReturn(expectedItemView);

        final ResponseEntity<ItemView> response = itemController.addItem(userId, itemCreateUpdateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemView, response.getBody());
    }

    @Test
    void addComment() {
        final Long userId = 1L;
        final Long itemId = 1L;
        final CommentRequest commentRequest = new CommentRequest();
        final CommentView expectedCommentView = new CommentView();

        when(itemService.createComment(userId, itemId, commentRequest))
                .thenReturn(expectedCommentView);

        final ResponseEntity<CommentView> response = itemController.addComment(itemId, userId, commentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCommentView, response.getBody());
    }

    @Test
    void updateItem_whenInvoked_thenResponseStatusOkWithItemViewInBody() {
        final Long userId = 1L;
        final Long itemId = 1L;
        final ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        final ItemView expectedItemView = new ItemView();

        when(itemService.update(userId, itemId, itemCreateUpdateDto))
                .thenReturn(expectedItemView);

        final ResponseEntity<ItemView> response = itemController.updateItem(userId, itemId, itemCreateUpdateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemView, response.getBody());
    }
}