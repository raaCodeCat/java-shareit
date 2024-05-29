package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Контроллер для работы с {@link Booking}.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    /**
     * Получение информации о бронировании по идентификатору (id).
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingView> getBooking(@RequestHeader(USER) Long userId,
                                  @PathVariable Long bookingId) {
        log.info("Получен запрос GET /bookings/{} от пользователя {}.", bookingId, userId);

        return ResponseEntity.ok(bookingService.getBooking(userId, bookingId));
    }

    /**
     * Получение списка бронирований пользователя.
     */
    @GetMapping
    public ResponseEntity<List<BookingView>> getBookingsByBooker(@RequestHeader(USER) Long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state,
                                                 @RequestParam(required = false) @Min(0) Integer from,
                                                 @RequestParam(required = false) @Min(1) @Max(20) Integer size) {
        log.info("Получен запрос GET /bookings со статусом {} от пользователя {}.", state, userId);

        return ResponseEntity.ok(bookingService.getBookingsByBooker(userId, state, from, size));
    }

    /**
     * Получение списка бронирований для владельца вещей.
     */
    @GetMapping("/owner")
    public ResponseEntity<List<BookingView>> getBookingsByOwner(@RequestHeader(USER) Long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                                @RequestParam(required = false) @Min(0) Integer from,
                                                @RequestParam(required = false) @Min(1) @Max(20) Integer size) {
        log.info("Получен запрос GET /bookings/owner со статусом {} от пользователя {}.", state, userId);

        return ResponseEntity.ok(bookingService.getBookingsByOwner(userId, state, from, size));
    }

    /**
     * Добавление нового бронирования.
     */
    @PostMapping
    public ResponseEntity<BookingView> addBooking(@RequestHeader(USER) Long userId,
                                  @RequestBody @Valid BookingRequest bookingDto) {
        log.info("Получен запрос POST /bookings от пользователя {}.", userId);

        return ResponseEntity.ok(bookingService.create(userId, bookingDto));
    }

    /**
     * Одобрение или отклонение нового бронирования.
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingView> approveBooking(@RequestHeader(USER) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam @NotNull Boolean approved) {
        log.info("Получен запрос PATCH /bookings/{} от пользователя {}", bookingId, userId);

        return ResponseEntity.ok(bookingService.approveBooking(userId, bookingId, approved));
    }
}
