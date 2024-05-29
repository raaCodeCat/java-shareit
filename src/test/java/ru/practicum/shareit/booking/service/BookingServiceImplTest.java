package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getBooking_whenInvoked_thenReturnBookingView() {
        final User owner = new User();
        owner.setId(1L);
        final User booker = new User();
        booker.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        final Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        final BookingView bookingView = BookingMapper.toBookingView(booking);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final BookingView actual = bookingService.getBooking(1L, 1L);

        assertNotNull(actual);
        assertEquals(bookingView, actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void getBooking_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBooking(1L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).findById(Mockito.anyLong());
        verify(itemRepository, never()).findById(Mockito.anyLong());
    }

    @Test
    void getBooking_whenInvalidBookingId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBooking(1L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Бронирование с идентификатором 1 не найдено",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, never()).findById(Mockito.anyLong());
    }

    @Test
    void getBooking_whenUserNotBookerAndUserNotItemOwner_thenNotFoundExceptionThrown() {
        final User user = new User();
        user.setId(3L);
        final User owner = new User();
        owner.setId(1L);
        final User booker = new User();
        booker.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        final Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBooking(3L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователю не доступна информация о бронировании с" +
                        " идентификатором 1", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void getBookingsByBooker_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingsByBooker(1L,BookingState.ALL, 0, 1)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).getBookingsByBooker(Mockito.anyLong(),
                Mockito.anyString());
        verify(bookingRepository, never()).getBookingsByBookerPageable(Mockito.anyLong(),
                Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    void getBookingsByBooker_whenInvokedNotPageable_thenReturnBookingViewList() {
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        final User booker = new User();
        booker.setId(1L);
        final Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        final List<Booking> bookings = List.of(booking);
        final List<BookingView> bookingViews = List.of(BookingMapper.toBookingView(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.getBookingsByBooker(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookings);

        final List<BookingView> actual = bookingService.getBookingsByBooker(1L, BookingState.ALL,
                null, null);

        assertNotNull(actual);
        assertEquals(bookingViews, actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1))
                .getBookingsByBooker(Mockito.anyLong(), Mockito.anyString());
        verify(bookingRepository, never()).getBookingsByBookerPageable(Mockito.anyLong(),
                Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    void getBookingsByBooker_whenInvokedPageable_thenReturnBookingViewList() {
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        final User booker = new User();
        booker.setId(1L);
        final Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        final List<Booking> bookings = List.of(booking);
        final List<BookingView> bookingViews = List.of(BookingMapper.toBookingView(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.getBookingsByBookerPageable(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(Pageable.class))).thenReturn(bookings);

        final List<BookingView> actual = bookingService.getBookingsByBooker(1L, BookingState.ALL,
                0, 1);

        assertNotNull(actual);
        assertEquals(bookingViews, actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never())
                .getBookingsByBooker(Mockito.anyLong(), Mockito.anyString());
        verify(bookingRepository, times(1))
                .getBookingsByBookerPageable(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.any(Pageable.class));
    }

    @Test
    void getBookingsByOwner_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingsByOwner(1L,BookingState.ALL, 0, 1)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).getBookingsByOwner(Mockito.anyLong(),
                Mockito.anyString());
        verify(bookingRepository, never()).getBookingsByOwnerPageable(Mockito.anyLong(),
                Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    void getBookingsByOwner_whenInvokedNotPageable_thenReturnBookingViewList() {
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        final User booker = new User();
        booker.setId(1L);
        final Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        final List<Booking> bookings = List.of(booking);
        final List<BookingView> bookingViews = List.of(BookingMapper.toBookingView(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.getBookingsByOwner(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookings);

        final List<BookingView> actual = bookingService.getBookingsByOwner(1L, BookingState.ALL,
                null, null);

        assertNotNull(actual);
        assertEquals(bookingViews, actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1))
                .getBookingsByOwner(Mockito.anyLong(), Mockito.anyString());
        verify(bookingRepository, never()).getBookingsByOwnerPageable(Mockito.anyLong(),
                Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    void getBookingsByOwner_whenInvokedPageable_thenReturnBookingViewList() {
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        final User booker = new User();
        booker.setId(1L);
        final Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        final List<Booking> bookings = List.of(booking);
        final List<BookingView> bookingViews = List.of(BookingMapper.toBookingView(booking));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.getBookingsByOwnerPageable(Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(Pageable.class))).thenReturn(bookings);

        final List<BookingView> actual = bookingService.getBookingsByOwner(1L, BookingState.ALL,
                0, 1);

        assertNotNull(actual);
        assertEquals(bookingViews, actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never())
                .getBookingsByOwner(Mockito.anyLong(), Mockito.anyString());
        verify(bookingRepository, times(1))
                .getBookingsByOwnerPageable(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.any(Pageable.class));
    }

    @Test
    void create_whenInvoked_thenCreateBookingAndReturnBookingView() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(owner);
        item.setAvailable(true);
        final BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        final Booking booking = BookingMapper.fromBookingRequest(bookingRequest);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        final BookingView actual = bookingService.create(1L, bookingRequest);

        assertNotNull(actual);
        assertEquals(BookingMapper.toBookingView(booking), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void create_whenInvalidUserId_thenNotFoundExceptionThrown() {
        final BookingRequest bookingRequest = new BookingRequest();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.create(1L, bookingRequest)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, never()).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void create_whenBookerIsItemOwner_thenNotFoundExceptionThrown() {
        final User user = new User();
        user.setId(1L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(user);
        item.setAvailable(true);
        final BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.create(1L, bookingRequest)
        );

        assertNotNull(exception);
        assertEquals("Попытка забронировать собственную вещь",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void create_whenItemNotUnavailable_thenBadRequestExceptionThrown() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(owner);
        item.setAvailable(false);
        final BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.create(1L, bookingRequest)
        );

        assertNotNull(exception);
        assertEquals("Вещь с itemId = 1 занята",
                exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void approveBooking_whenInvoked_thenUpdateBookingAndReturnBookingView() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(owner);
        item.setAvailable(true);
        final Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        final BookingView actual = bookingService.approveBooking(2L, 1L, true);

        assertNotNull(actual);
        assertEquals(BookingMapper.toBookingView(booking), actual);

        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).save(Mockito.any(Booking.class));
    }

    @Test
    void approveBooking_whenInvalidBookingId_thenNotFoundExceptionThrown() {
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.approveBooking(1L, 1L, true)
        );

        assertNotNull(exception);
        assertEquals("Бронирование с идентификатором 1 не найдено", exception.getMessage());

        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, never()).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void approveBooking_whenUserNotOwnerOfItem_thenNotFoundExceptionThrown() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(owner);
        item.setAvailable(true);
        final Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.approveBooking(3L, 1L, true)
        );

        assertNotNull(exception);
        assertEquals("Пользователь не владеет вещью с идентификатором 1", exception.getMessage());

        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }

    @Test
    void approveBooking_whenBookingAlreadyAvailable_thenBadRequestExceptionThrown() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(2L);
        final Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setOwner(owner);
        item.setAvailable(true);
        final Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        final BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.approveBooking(2L, 1L, true)
        );

        assertNotNull(exception);
        assertEquals("Бронирование уже одобрено или отклонено", exception.getMessage());

        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
    }
}