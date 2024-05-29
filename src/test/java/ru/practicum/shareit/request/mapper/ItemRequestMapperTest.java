package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {
    @Test
    void toItemRequestView() {
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestView itemRequestView = new ItemRequestView();

        ItemRequestView actual = ItemRequestMapper.toItemRequestView(itemRequest);

        assertNotNull(actual);
        assertEquals(itemRequestView, actual);
    }

    @Test
    void makeItemRequest() {
        LocalDateTime dateTime = LocalDateTime.now();
        User user = new User();
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setUser(user);
        itemRequest.setCreated(dateTime);

        ItemRequest actual = ItemRequestMapper.makeItemRequest(user, itemRequestDto, dateTime);

        assertNotNull(actual);
        assertEquals(itemRequest, actual);
    }

    @Test
    void toItemRequestUserView() {
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestUserView itemRequestUserView = new ItemRequestUserView();
        itemRequestUserView.setItems(Collections.emptyList());

        ItemRequestUserView actual = ItemRequestMapper.toItemRequestUserView(itemRequest);

        assertNotNull(actual);
        assertEquals(itemRequestUserView, actual);
    }

    @Test
    void toCollectionOfItemRequestUserView() {
        List<ItemRequest> itemRequests = List.of(new ItemRequest());
        ItemRequestUserView itemRequestUserView = new ItemRequestUserView();
        itemRequestUserView.setItems(Collections.emptyList());
        List<ItemRequestUserView> itemRequestUserViews = List.of(itemRequestUserView);

        List<ItemRequestUserView> actual = ItemRequestMapper.toItemRequestUserView(itemRequests);

        assertNotNull(actual);
        assertEquals(itemRequestUserViews, actual);
    }
}