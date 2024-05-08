package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Сервис для работы с {@link Item}.
 */
public interface ItemService {
    List<ItemDto> getUserItems(Long userId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> searchItems(String searchText);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, ItemDto itemDto);
}
