package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Параметры для POST, PATCH методов {@link Booking}.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class BookingRequest {
    /**
     * Идентификатор бронирования вещи.
     */
    private Long id;

    /**
     * Идентификатор вещи.
     */
    @NotNull
    private Long itemId;

    /**
     * Дата начала бронирования.
     */
    @NotNull
    @Future
    private LocalDateTime start;

    /**
     * Дата окончания бронирования.
     */
    @NotNull
    @Future
    private LocalDateTime end;

    @AssertTrue(message = "Дата окончания бронирования не должна быть меньше даты начала бронирования")
    private boolean isEndDateGreater() {
        if (start != null && end != null && !end.equals(start)) {
            return end.isAfter(start);
        }
        return true;
    }

    @AssertTrue(message = "Дата окончания бронирования не должна быть равна дате начала бронирования")
    private boolean isStartNotEqualEnd() {
        if (start != null && end != null) {
            return !end.equals(start);
        }
        return true;
    }
}
