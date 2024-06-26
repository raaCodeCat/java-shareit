package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import java.util.List;

public interface BookingService {
    BookingView getBooking(Long userId, long bookingId);

    List<BookingView> getBookingsByBooker(Long userId, BookingState state, Integer from, Integer size);

    List<BookingView> getBookingsByOwner(Long userId, BookingState state, Integer from, Integer size);

    BookingView create(Long userId, BookingRequest bookingDto);

    BookingView approveBooking(Long userId, Long bookingId, Boolean approved);
}
