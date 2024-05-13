package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Хранилище в памяти для работы с {@link Item}.
 */
@Component
@Slf4j
public class ItemStorageInMemory implements ItemDao {
    private Long currentId = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException(String.format("Вещь с itemId = %d не найден", id));
        }

        return items.get(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        String searchText = text.toLowerCase();

        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        currentId++;
        item.setId(currentId);
        items.put(currentId, item);
        log.info("Вещь добавлена");

        return item;
    }

    @Override
    public Item update(Item item) {
        Long id = item.getId();
        String newName = item.getName();
        String newDescription = item.getDescription();
        Boolean newAvailable = item.getAvailable();
        Item itemInMemory = items.get(id);

        if (newName != null && !newName.equals(itemInMemory.getName())) {
            itemInMemory.setName(newName);
        }

        if (newDescription != null && !newDescription.equals(itemInMemory.getDescription())) {
            itemInMemory.setDescription(newDescription);
        }

        if (newAvailable != null && !newAvailable.equals(itemInMemory.getAvailable())) {
            itemInMemory.setAvailable(newAvailable);
        }

        log.info("Вещь обновлена");

        return itemInMemory;
    }
}
