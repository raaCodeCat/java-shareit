package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Сервис для работы с {@link Item}.
 */
public interface ItemService {
    List<Item> getUserItems(Long userId);

    Item getItemById(Long itemId);

    List<Item> searchItems(String searchText);

    Item create(Long userId, Item item);

    Item update(Long userId, Item item);
}
