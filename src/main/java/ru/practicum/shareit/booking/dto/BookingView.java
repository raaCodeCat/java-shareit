package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.shareitutils.dto.IdFieldView;
import ru.practicum.shareit.shareitutils.dto.IdNameFieldView;
import java.time.LocalDateTime;

/**
 * DTO для ответа {@link Booking}.
 */
@Getter
@Setter
@AllArgsConstructor
public class BookingView {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private IdFieldView booker;

    private IdNameFieldView item;
}
