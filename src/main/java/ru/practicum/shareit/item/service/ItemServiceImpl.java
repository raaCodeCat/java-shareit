package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortView;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
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
    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ItemView> getUserItems(Long userId) {
        log.info("Запрошен список вещей пользователя с id = {}", userId);

        List<ItemView> items = ItemMapper.toItemView(itemRepository.findItemsByOwnerIdOrderById(userId));

        for (ItemView item : items) {
            addBookingInfoToItemView(item);
            addCommentsToItemView(item);
        }

        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemView getItemById(Long userId, Long itemId) {
        log.info("Запрошена вещь с itemId = {}", itemId);

        Item item = findItemById(itemId);
        ItemView itemView = ItemMapper.toItemView(findItemById(itemId));

        if (Objects.equals(userId, item.getOwner().getId())) {
            addBookingInfoToItemView(itemView);
        }

        addCommentsToItemView(itemView);

        return itemView;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemView> searchItems(String searchText) {
        log.info("Запрошен поиск вещей по тексту = \"{}\"", searchText);

        if (searchText.isBlank()) {
            return new ArrayList<>();
        }

        return ItemMapper.toItemView(
                itemRepository.findItemsByAvailableIsTrueAndNameContainingIgnoreCaseOrAvailableIsTrueAndDescriptionContainingIgnoreCase(
                        searchText, searchText));
    }

    @Override
    @Transactional
    public ItemView create(Long userId, ItemRequest itemRequest) {
        log.info("Попытка добавить вещь {}", itemRequest);

        User user = UserMapper.fromUserDto(userService.getById(userId));
        Item item = ItemMapper.fromItemRequest(itemRequest);
        item.setOwner(user);

        return ItemMapper.toItemView(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemView update(Long userId, ItemRequest itemRequest) {
        log.info("Попытка обновить вещь {}", itemRequest);

        Long itemId = itemRequest.getId();
        String newName = itemRequest.getName();
        String newDescription = itemRequest.getDescription();
        Boolean newAvailable = itemRequest.getAvailable();
        Item itemForUpdate = findItemById(itemId);

        if (!Objects.equals(itemForUpdate.getOwner().getId(), userId)) {
            throw new NotFoundException(
                    String.format("Пользователь не владеет вещью с идентификатором %d", itemId)
            );
        }

        if (newName != null) {
            itemForUpdate.setName(newName);
        }

        if (newDescription != null) {
            itemForUpdate.setDescription(newDescription);
        }

        if (newAvailable != null) {
            itemForUpdate.setAvailable(newAvailable);
        }

        return ItemMapper.toItemView(itemRepository.save(itemForUpdate));
    }

    @Override
    @Transactional
    public CommentView createComment(Long userId, Long itemId, CommentRequest commentRequest) {
        log.info("Добавление отзыва {} на вещь с идентификатором {}", commentRequest, itemId);

        LocalDateTime dateTime = LocalDateTime.now();
        User user = findUserById(userId);
        Item item = findItemById(itemId);

        Optional<Booking> bookingOpt = bookingRepository.findFirstByItemIdAndBookerIdAndEndDtBeforeAndStatusIsNot(
                itemId, userId, dateTime, BookingStatus.REJECTED
        );

        if (bookingOpt.isEmpty()) {
            throw new BadRequestException(
                    String.format("У пользователя нет завершенного бронирования вещи с идентификатором %d", itemId)
            );
        }

        Comment comment = new Comment(null, commentRequest.getText(), item, user, dateTime);

        return CommentMapper.toCommentView(commentRepository.save(comment));
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Вещь с идентификатором %d не найдена", id)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c идентификатором %d не найдена", userId)));
    }

    private void addBookingInfoToItemView(ItemView item) {
        BookingShortView lastBooking = bookingRepository
                .findFirstByItemIdAndStartDtBeforeAndStatusIsNotOrderByEndDtDescIdAsc(
                        item.getId(), LocalDateTime.now(), BookingStatus.REJECTED)
                .map(BookingMapper::toBookingShortView)
                .orElse(null);

        BookingShortView nextBooking  = bookingRepository
                .findFirstByItemIdAndStartDtAfterAndStatusIsNotOrderByStartDtAscIdAsc(
                        item.getId(), LocalDateTime.now(), BookingStatus.REJECTED)
                .map(BookingMapper::toBookingShortView)
                .orElse(null);

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
    }

    private void addCommentsToItemView(ItemView item) {
        List<Comment> comments;
        comments = commentRepository.findByItemIdOrderByCreatedAsc(item.getId());
        item.setComments(CommentMapper.toCommentView(comments));
    }
}
