package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Сервис для работы с {@link Item}.
 */
public interface ItemService {
    List<ItemView> getUserItems(Long userId);

    ItemView getItemById(Long userId, Long itemId);

    List<ItemView> searchItems(String searchText);

    ItemView create(Long userId, ItemCreateUpdateDto itemCreateUpdateDto);

    ItemView update(Long userId, Long itemId, ItemCreateUpdateDto itemCreateUpdateDto);

    CommentView createComment(Long userId, Long itemId, CommentRequest commentRequest);
}
