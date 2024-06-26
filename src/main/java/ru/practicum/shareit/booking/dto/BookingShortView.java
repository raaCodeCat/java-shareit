package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

/**
 * DTO для ответа, сокращенный вид {@link Booking}.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
