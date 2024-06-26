package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.service.BookingService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    @Test
    void getBooking_whenInvoked_thenResponseStatusOkWithBookingViewInBody() {
        final BookingView expectedBookingView = new BookingView();
        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(expectedBookingView);

        final ResponseEntity<BookingView> actual = bookingController.getBooking(1L, 1L);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBookingView, actual.getBody());
    }

    @Test
    void getBookingsByBooker_whenInvoked_thenResponseStatusOkWithCollectionBookingViewInBody() {
        final List<BookingView> expectedBookingsView = List.of(new BookingView());
        when(bookingService.getBookingsByBooker(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(expectedBookingsView);

        final ResponseEntity<List<BookingView>> actual = bookingController.getBookingsByBooker(1L,
                BookingState.ALL, 0, 1);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBookingsView, actual.getBody());
    }

    @Test
    void getBookingsByOwner_whenInvoked_thenResponseStatusOkWithCollectionBookingViewInBody() {
        final List<BookingView> expectedBookingsView = List.of(new BookingView());
        when(bookingService.getBookingsByOwner(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(expectedBookingsView);

        final ResponseEntity<List<BookingView>> actual = bookingController.getBookingsByOwner(1L,
                BookingState.ALL, 0, 1);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBookingsView, actual.getBody());
    }

    @Test
    void addBooking_whenInvoked_thenResponseStatusOkWithBookingViewInBody() {
        final BookingView expectedBookingView = new BookingView();
        when(bookingService.create(Mockito.anyLong(), Mockito.any(BookingRequest.class)))
                .thenReturn(expectedBookingView);

        final ResponseEntity<BookingView> actual = bookingController.addBooking(1L, new BookingRequest());

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBookingView, actual.getBody());
    }

    @Test
    void approveBooking_whenInvoked_thenResponseStatusOkWithBookingViewInBody() {
        final Long userId = 1L;
        final Long bookingId = 1L;
        final Boolean approved = true;
        final BookingView expectedBookingView = new BookingView();
        when(bookingService.approveBooking(userId, bookingId, approved))
                .thenReturn(expectedBookingView);

        ResponseEntity<BookingView> actual = bookingController.approveBooking(userId, bookingId, approved);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expectedBookingView, actual.getBody());
    }
}