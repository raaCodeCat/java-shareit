package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * DAO для работы с {@link Item}.
 */
public interface ItemDao {
    List<Item> getUserItems(Long userId);

    Item getById(Long itemId);

    List<Item> searchItems(String searchText);

    Item create(Item item);

    Item update(Item item);
}
