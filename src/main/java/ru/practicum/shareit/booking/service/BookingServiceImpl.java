package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public BookingView getBooking(Long userId, long bookingId) {
        log.info("Получений информации о бронировании с идентификатором {}", bookingId);

        findUserById(userId);
        Booking booking = findBookingById(bookingId);
        Item item = findItemById(booking.getItem().getId());

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(item.getOwner().getId())) {
            throw new NotFoundException(
                    String.format("Пользователю не доступна информация о бронировании с идентификатором %d", bookingId)
            );
        }

        return BookingMapper.toBookingView(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingView> getBookingsByBooker(Long userId, BookingState state) {
        log.info("Запрос списка бронирований со статусом {} пользователем с идентификатором {}", state, userId);

        findUserById(userId);

        return BookingMapper.toBookingView(bookingRepository.getBookingsByBooker(userId, state.name()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingView> getBookingsByOwner(Long userId, BookingState state) {
        log.info("Запрос списка бронирований со статусом {} владельцем с идентификатором {}", state, userId);

        findUserById(userId);

        return BookingMapper.toBookingView(bookingRepository.getBookingsByOwner(userId, state.name()));
    }

    @Override
    @Transactional
    public BookingView create(Long userId, BookingRequest bookingRequest) {
        log.info("Попытка создать бронирование {} пользователем с идентификатором {}", bookingRequest, userId);

        User user = findUserById(userId);
        Long itemId = bookingRequest.getItemId();
        Item item = findItemById(itemId);

        if (user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Попытка забронировать собственную вещь");
        }

        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с itemId = %d занята", itemId));
        }

        Booking booking = BookingMapper.fromBookingRequest(bookingRequest);
        User booker = new User();

        booker.setId(userId);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Booking newBooking = bookingRepository.save(booking);
        newBooking.setItem(item);

        return BookingMapper.toBookingView(newBooking);
    }

    @Override
    @Transactional
    public BookingView approveBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Попытка одобрить или отказать в бронировании");

        Booking bookingForUpdate = findBookingById(bookingId);
        Item item = findItemById(bookingForUpdate.getItem().getId());

        if (!userId.equals(item.getOwner().getId())) {
            throw new NotFoundException(
                    String.format("Пользователь не владеет вещью с идентификатором %d", item.getId()));
        }

        if (!BookingStatus.WAITING.equals(bookingForUpdate.getStatus())) {
            throw new BadRequestException("Бронирование уже одобрено или отклонено");
        }

        bookingForUpdate.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(bookingForUpdate);
        updatedBooking.setItem(item);

        return BookingMapper.toBookingView(updatedBooking);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c идентификатором %d не найдена", userId)));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь с идентификатором %d не найдена", itemId)));
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование с идентификатором %d не найдено", bookingId)));
    }
}
