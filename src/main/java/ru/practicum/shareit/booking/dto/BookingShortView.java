package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;

/**
 * DTO для ответа, сокращенный вид {@link Booking}.
 */
@Getter
@Setter
@AllArgsConstructor
public class BookingShortView {
    /**
     * Идентификатор бронирования вещи.
     */
    private Long id;

    /**
     * Идентификатор забронировавшего пользователя.
     */
    private Long bookerId;
}
