package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookingRepositoryIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private User booker;
    private User owner;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2024, 8, 1, 10, 30);
        LocalDateTime end = LocalDateTime.of(2024, 8, 2, 10, 30);
        owner = new User(null, "owner", "user@email.com");
        booker = new User(null, "booker", "booker@email.com");
        userRepository.save(owner);
        userRepository.save(booker);
        item1 = new Item(null, "name_1", "description_1", true, owner, null);
        item2 = new Item(null, "name_2", "description_2", true, owner, null);
        itemRepository.save(item1);
        itemRepository.save(item2);
        booking1 = new Booking(null, start, end, item1, booker, BookingStatus.WAITING);
        booking2 = new Booking(null, start.plusDays(1), end.plusDays(1), item2, booker, BookingStatus.WAITING);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    @Test
    void getBookingsByBooker() {
        List<Booking> bookings = bookingRepository.getBookingsByBooker(booker.getId(), BookingState.ALL.name());

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(booking2, bookings.get(0));
        assertEquals(booking1, bookings.get(1));
    }

    @Test
    void getBookingsByBookerPageable() {
        List<Booking> bookings = bookingRepository.getBookingsByBookerPageable(booker.getId(), BookingState.ALL.name(),
                PageRequest.of(0, 1));

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2, bookings.get(0));
    }

    @Test
    void getBookingsByOwner() {
        List<Booking> bookings = bookingRepository.getBookingsByOwner(owner.getId(), BookingState.ALL.name());

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(booking2, bookings.get(0));
        assertEquals(booking1, bookings.get(1));
    }

    @Test
    void getBookingsByOwnerPageable() {
        List<Booking> bookings = bookingRepository.getBookingsByOwnerPageable(owner.getId(), BookingState.ALL.name(),
                PageRequest.of(0, 1));

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2, bookings.get(0));
    }

    @Test
    void findLastBookingByItemIds() {
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 8, 10, 10, 30);
        List<Booking> bookings = bookingRepository.findLastBookingByItemIds(List.of(item1.getId(), item2.getId()),
                currentDateTime);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    void findNextBookingByItemIds() {
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 7, 10, 10, 30);
        List<Booking> bookings = bookingRepository.findNextBookingByItemIds(List.of(item1.getId(), item2.getId()),
                currentDateTime);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
    }

    @Test
    void findBookingForAllowCommentPageable_whenBookingNotApproved_thenReturnEmptyCollection() {
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 8, 10, 10, 30);
        List<Booking> bookings = bookingRepository.findBookingForAllowCommentPageable(item1.getId(),
                booker.getId(), currentDateTime, PageRequest.of(0, 10));

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findBookingForAllowCommentPageable_whenBookingApproved_thenReturnNotEmptyCollection() {
        booking1.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking1);
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 8, 10, 10, 30);
        List<Booking> bookings = bookingRepository.findBookingForAllowCommentPageable(item1.getId(),
                booker.getId(), currentDateTime, PageRequest.of(0, 10));

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1, bookings.get(0));
    }
}