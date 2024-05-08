package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public List<ItemDto> getUserItems(Long userId) {
        log.info("Запрошен список вещей пользователя с id = {}", userId);

        return ItemMapper.toItemDto(itemDao.getUserItems(userId));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("Запрошена вещь с itemId = {}", itemId);

        return ItemMapper.toItemDto(itemDao.getById(itemId));
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        log.info("Запрошен поиск вещей по тексту = \"{}\"", searchText);

        if (searchText.isBlank()) {
            return new ArrayList<>();
        }

        return ItemMapper.toItemDto(itemDao.searchItems(searchText));
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        log.info("Попытка добавить вещь {}", itemDto);

        User user = UserMapper.fromUserDto(userService.getById(userId));
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(user);

        return ItemMapper.toItemDto(itemDao.create(item));
    }

    @Override
    public ItemDto update(Long userId, ItemDto itemDto) {
        log.info("Попытка обновить вещь {}", itemDto);

        Long itemId = itemDto.getId();
        Item currenItem = itemDao.getById(itemId);

        if (!Objects.equals(currenItem.getOwner().getId(), userId)) {
            throw new NotFoundException(
                    String.format("Пользователь не владеет вещью с id = %d", itemId)
            );
        }

        Item item = ItemMapper.fromItemDto(itemDto);

        return ItemMapper.toItemDto(itemDao.update(item));
    }
}
