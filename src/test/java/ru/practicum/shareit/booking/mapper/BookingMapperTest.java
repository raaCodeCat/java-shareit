package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingShortView;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shareitutils.dto.IdFieldView;
import ru.practicum.shareit.shareitutils.dto.IdNameFieldView;
import ru.practicum.shareit.user.model.User;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toBookingView() {
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        BookingView bookingView = new BookingView();
        bookingView.setBooker(new IdFieldView(user.getId()));
        bookingView.setItem(new IdNameFieldView(item.getId(), item.getName()));

        BookingView actual = BookingMapper.toBookingView(booking);

        assertNotNull(actual);
        assertEquals(bookingView, actual);
    }

    @Test
    void toCollectionOfBookingView() {
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        BookingView bookingView = new BookingView();
        bookingView.setBooker(new IdFieldView(user.getId()));
        bookingView.setItem(new IdNameFieldView(item.getId(), item.getName()));
        List<Booking> bookings = List.of(booking);
        List<BookingView> bookingViews = List.of(bookingView);

        List<BookingView> actual = BookingMapper.toBookingView(bookings);

        assertNotNull(actual);
        assertEquals(bookingViews, actual);
    }

    @Test
    void fromBookingRequest() {
        BookingRequest bookingRequest = new BookingRequest();
        Booking booking = new Booking();
        booking.setItem(new Item());

        Booking actual = BookingMapper.fromBookingRequest(bookingRequest);

        assertNotNull(actual);
        assertEquals(booking, actual);
    }

    @Test
    void toBookingShortView() {
        User user = new User();
        Item item = new Item();
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        BookingShortView bookingShortView = new BookingShortView();

        BookingShortView actual = BookingMapper.toBookingShortView(booking);

        assertNotNull(actual);
        assertEquals(bookingShortView, actual);
    }
}