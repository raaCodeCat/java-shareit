package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Сервис для работы с {@link ItemRequest}.
 */
public interface ItemRequestService {
    ItemRequestView create(Long userId, ItemRequestDto requestDto);

    List<ItemRequestUserView> getItemRequestsByUserId(Long userId);

    ItemRequestUserView getItemRequestById(Long userId, Long id);

    List<ItemRequestUserView> getItemRequestsPageable(Long userId, Integer from, Integer size);
}
