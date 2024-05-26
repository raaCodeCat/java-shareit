package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingShortView;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shareitutils.dto.IdFieldView;
import ru.practicum.shareit.shareitutils.dto.IdNameFieldView;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingView toBookingView(Booking source) {
        return new BookingView(
                source.getId(),
                source.getStartDt(),
                source.getEndDt(),
                source.getStatus(),
                new IdFieldView(source.getBooker().getId()),
                new IdNameFieldView(source.getItem().getId(), source.getItem().getName()));
    }

    public static List<BookingView> toBookingView(List<Booking> source) {
        return source.stream()
                .map(BookingMapper::toBookingView)
                .collect(Collectors.toList());
    }

    public static Booking fromBookingRequest(BookingRequest source) {
        Item item = new Item();
        item.setId(source.getItemId());

        return new Booking(source.getId(), source.getStart(), source.getEnd(), item, null, null);
    }

    public static BookingShortView toBookingShortView(Booking source) {
        return new BookingShortView(source.getId(), source.getBooker().getId());
    }
}
