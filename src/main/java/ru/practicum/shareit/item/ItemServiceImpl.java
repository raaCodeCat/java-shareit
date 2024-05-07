package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Сервис для работы с {@link Item}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;

    private final UserService userService;

    @Override
    public List<Item> getUserItems(Long userId) {
        log.info("Запрошен список вещей пользователя с id = {}", userId);

        return itemDao.getUserItems(userId);
    }

    @Override
    public Item getItemById(Long itemId) {
        log.info("Запрошена вещь с itemId = {}", itemId);

        Optional<Item> itemOptional = itemDao.getById(itemId);

        if (itemOptional.isEmpty()) {
            throw new NotFoundException(String.format("Вещь с itemId = %d не найден", itemId));
        }

        return itemOptional.get();
    }

    @Override
    public List<Item> searchItems(String searchText) {
        log.info("Запрошен поиск вещей по тексту = \"{}\"", searchText);

        if (searchText.isBlank()) {
            return new ArrayList<>();
        }

        return itemDao.searchItems(searchText);
    }

    @Override
    public Item create(Long userId, Item item) {
        log.info("Попытка добавить вещь {}", item);
        User user = userService.getById(userId);
        item.setOwner(user);

        return itemDao.create(item);
    }

    @Override
    public Item update(Long userId, Item item) {
        log.info("Попытка обновить вещь {}", item);

        Long itemId = item.getId();
        Item currenItem = getItemById(itemId);

        if (!Objects.equals(currenItem.getOwner().getId(), userId)) {
            throw new NotFoundException(
                    String.format("Пользователь не владеет вещью с id = %d", itemId)
            );
        }

        return itemDao.update(item);
    }
}
