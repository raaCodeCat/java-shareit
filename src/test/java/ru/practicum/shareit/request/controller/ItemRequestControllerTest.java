package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.service.ItemRequestService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void addItemRequest_whenInvoked_thenResponseStatusOkWithItemRequestViewInBody() {
        final ItemRequestView itemRequestView = new ItemRequestView();
        when(itemRequestService.create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(itemRequestView);

        final ResponseEntity<ItemRequestView> actual = itemRequestController.addItemRequest(1L,
                new ItemRequestDto());

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(itemRequestView, actual.getBody());
    }

    @Test
    void getUserItemRequest_whenInvoked_thenResponseStatusOkWithCollectionItemRequestUserViewInBody() {
        final List<ItemRequestUserView> itemRequestUserViews = List.of(new ItemRequestUserView());
        when(itemRequestService.getItemRequestsByUserId(1L)).thenReturn(itemRequestUserViews);

        final ResponseEntity<List<ItemRequestUserView>> actual = itemRequestController.getUserItemRequest(1L);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(itemRequestUserViews, actual.getBody());
    }

    @Test
    void getItemRequestById_whenInvoked_thenResponseStatusOkWithItemRequestUserViewInBody() {
        final ItemRequestUserView itemRequestUserView = new ItemRequestUserView();
        when(itemRequestService.getItemRequestById(1L, 1L)).thenReturn(itemRequestUserView);

        final ResponseEntity<ItemRequestUserView> actual = itemRequestController.getItemRequestById(1L, 1L);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(itemRequestUserView, actual.getBody());
    }

    @Test
    void getItemRequestsPageable_whenInvoked_thenResponseStatusOkWithCollectionItemRequestUserViewInBody() {
        final List<ItemRequestUserView> itemRequestUserViews = List.of(new ItemRequestUserView());
        when(itemRequestService.getItemRequestsPageable(1L, 0, 1)).thenReturn(itemRequestUserViews);

        final ResponseEntity<List<ItemRequestUserView>> actual = itemRequestController.getItemRequestsPageable(1L, 0, 1);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(itemRequestUserViews, actual.getBody());
    }
}