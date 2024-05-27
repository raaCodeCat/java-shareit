package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        List<ItemView> itemViews = ItemMapper.toItemView(
                itemRepository.findItemsByOwnerIdOrderById(userId));

        addBookingInfoToItemViewList(itemViews);
        addCommentsToItemViewList(itemViews);

        return itemViews;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemView getItemById(Long userId, Long itemId) {
        log.info("Запрошена вещь с itemId = {}", itemId);

        Item item = findItemById(itemId);
        ItemView itemView = ItemMapper.toItemView(findItemById(itemId));

        List<ItemView> itemViews = List.of(itemView);

        if (Objects.equals(userId, item.getOwner().getId())) {
            addBookingInfoToItemViewList(itemViews);
        }

        addCommentsToItemViewList(itemViews);

        return itemViews.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemView> searchItems(String searchText) {
        log.info("Запрошен поиск вещей по тексту = \"{}\"", searchText);

        if (searchText.isBlank()) {
            return new ArrayList<>();
        }

        return ItemMapper.toItemView(
                itemRepository.findItemBySearchText(searchText));
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
        Pageable pageable = PageRequest.of(0, 1);

        List<Booking> bookingForAllowComment = bookingRepository.findBookingForAllowCommentPageable(
                itemId, userId, dateTime, pageable);

        if (bookingForAllowComment.size() == 0) {
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

    private void addBookingInfoToItemViewList(List<ItemView> items) {
        List<Long> itemIds = items.stream().map(ItemView::getId).collect(Collectors.toList());

        List<Booking> lastBookings = bookingRepository.findLastBookingByItemIds(
                itemIds, LocalDateTime.now());

        List<Booking> nextBookings = bookingRepository.findNextBookingByItemIds(
                itemIds, LocalDateTime.now());

        for (ItemView item : items) {
            BookingShortView lastBooking = lastBookings.stream()
                    .filter(booking -> item.getId().equals(booking.getItem().getId()))
                    .max(Comparator.comparing(Booking::getEndDt))
                    .map(BookingMapper::toBookingShortView).orElse(null);

            BookingShortView nextBooking = nextBookings.stream()
                    .filter(booking -> item.getId().equals(booking.getItem().getId()))
                    .min(Comparator.comparing(Booking::getStartDt))
                    .map(BookingMapper::toBookingShortView).orElse(null);

            item.setLastBooking(lastBooking);
            item.setNextBooking(nextBooking);
        }
    }

    private void addCommentsToItemViewList(List<ItemView> items) {
        List<Long> itemIds = items.stream().map(ItemView::getId).collect(Collectors.toList());

        List<Comment> comments = commentRepository.findCommentByIds(itemIds);

        for (ItemView item : items) {
            List<CommentView> commentViews = comments.stream()
                    .filter(comment -> item.getId().equals(comment.getItem().getId()))
                    .sorted(Comparator.comparing(Comment::getCreated))
                    .map(CommentMapper::toCommentView).collect(Collectors.toList());

            item.setComments(commentViews);
        }
    }
}
