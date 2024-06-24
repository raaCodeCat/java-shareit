package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private static final String USER = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void getBooking_whenValid_thenResponseStatusOkAndBookingViewInBody() {
        Long bookingId = 1L;
        Long userId = 1L;
        BookingView booking = new BookingView();
        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong())).thenReturn(booking);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId).header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(booking), result);

        verify(bookingService, times(1)).getBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getBooking_whenBookingIdIsNotValid_thenResponseStatusNotFound() {
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("Booking not found"));

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId).header(USER, userId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"Booking not found\"}", result);

        verify(bookingService, times(1)).getBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getBooking_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new NotFoundException("User not found"));

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId).header(USER, userId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"User not found\"}", result);

        verify(bookingService, times(1)).getBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getBooking_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long bookingId = 1L;

        mockMvc.perform(get("/bookings/{bookingId}", bookingId))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getBooking(Mockito.anyLong(), Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenValid_thenResponseStatusOkAndCollectionBookingViewInBody() {
        List<BookingView> bookings = List.of(new BookingView());
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(bookingService.getBookingsByBooker(userId, state, from, size)).thenReturn(bookings);

        String result = mockMvc.perform(get("/bookings")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(bookings), result);

        verify(bookingService, times(1)).getBookingsByBooker(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(bookingService.getBookingsByBooker(userId, state, from, size)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/bookings")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header(USER, userId))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingsByBooker(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;

        mockMvc.perform(get("/bookings")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getBookingsByBooker(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenValid_thenResponseStatusOkAndCollectionBookingViewInBody() {
        List<BookingView> bookings = List.of(new BookingView());
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(bookingService.getBookingsByOwner(userId, state, from, size)).thenReturn(bookings);

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(bookings), result);

        verify(bookingService, times(1)).getBookingsByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(bookingService.getBookingsByOwner(userId, state, from, size)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString())
                        .header(USER, userId))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingsByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        BookingState state = BookingState.ALL;
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state.toString())
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).getBookingsByOwner(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void addBooking_whenValid_thenResponseStatusOkAndBookingViewInBody() {
        Long userId = 1L;
        Long itemId = 2L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingRequest bookingToCreate = new BookingRequest();
        bookingToCreate.setItemId(itemId);
        bookingToCreate.setStart(start);
        bookingToCreate.setEnd(end);
        BookingView booking = new BookingView();
        when(bookingService.create(Mockito.anyLong(), Mockito.any(BookingRequest.class))).thenReturn(booking);

        String result = mockMvc.perform(post("/bookings").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(booking), result);

        verify(bookingService, times(1)).create(Mockito.anyLong(), Mockito.any(BookingRequest.class));
    }

    @SneakyThrows
    @Test
    void addBooking_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 2L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingRequest bookingToCreate = new BookingRequest();
        bookingToCreate.setItemId(itemId);
        bookingToCreate.setStart(start);
        bookingToCreate.setEnd(end);
        when(bookingService.create(Mockito.anyLong(), Mockito.any(BookingRequest.class)))
                .thenThrow(new NotFoundException("User not found"));

        String result = mockMvc.perform(post("/bookings").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"User not found\"}", result);

        verify(bookingService, times(1)).create(Mockito.anyLong(), Mockito.any(BookingRequest.class));
    }

    @SneakyThrows
    @Test
    void addBooking_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long itemId = 2L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingRequest bookingToCreate = new BookingRequest();
        bookingToCreate.setItemId(itemId);
        bookingToCreate.setStart(start);
        bookingToCreate.setEnd(end);

        mockMvc.perform(post("/bookings").contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).create(Mockito.anyLong(), Mockito.any(BookingRequest.class));
    }

    @SneakyThrows
    @Test
    void addBooking_whenItemIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 2L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        BookingRequest bookingToCreate = new BookingRequest();
        bookingToCreate.setItemId(itemId);
        bookingToCreate.setStart(start);
        bookingToCreate.setEnd(end);
        when(bookingService.create(Mockito.anyLong(), Mockito.any(BookingRequest.class)))
                .thenThrow(new NotFoundException("Item not found"));

        String result = mockMvc.perform(post("/bookings").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"Item not found\"}", result);

        verify(bookingService, times(1)).create(Mockito.anyLong(), Mockito.any(BookingRequest.class));
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingRequestIsNotValid_thenResponseStatusBadRequest() {
        Long userId = 1L;
        BookingRequest bookingToCreate = new BookingRequest();

        mockMvc.perform(post("/bookings").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingToCreate)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).create(Mockito.anyLong(), Mockito.any(BookingRequest.class));
    }

    @SneakyThrows
    @Test
    void approveBooking_whenValid_thenResponseStatusOkAndBookingViewInBody() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        BookingView booking = new BookingView();
        when(bookingService.approveBooking(userId, bookingId, approved)).thenReturn(booking);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header(USER, userId)
                        .param("approved", approved.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(booking), result);

        verify(bookingService, times(1)).approveBooking(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approveBooking_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        when(bookingService.approveBooking(userId, bookingId, approved))
                .thenThrow(new NotFoundException("User not found"));

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header(USER, userId)
                        .param("approved", approved.toString()))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"User not found\"}", result);

        verify(bookingService, times(1)).approveBooking(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approveBooking_whenBookingIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        when(bookingService.approveBooking(userId, bookingId, approved))
                .thenThrow(new NotFoundException("Booking not found"));

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header(USER, userId)
                        .param("approved", approved.toString()))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"Booking not found\"}", result);

        verify(bookingService, times(1)).approveBooking(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approveBooking_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", approved.toString()))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).approveBooking(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approveBooking_whenParamApprovedIsMissing_thenResponseStatusInternalServerError() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header(USER, userId))
                .andExpect(status().isInternalServerError());

        verify(bookingService, never()).approveBooking(userId, bookingId, approved);
    }

    @SneakyThrows
    @Test
    void approveBooking_whenBookingAlreadyApproved_thenResponseBadRequestException() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        when(bookingService.approveBooking(userId, bookingId, approved))
                .thenThrow(new BadRequestException("Booking already approved"));

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header(USER, userId)
                        .param("approved", approved.toString()))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"Booking already approved\"}", result);

        verify(bookingService, times(1)).approveBooking(userId, bookingId, approved);
    }
}