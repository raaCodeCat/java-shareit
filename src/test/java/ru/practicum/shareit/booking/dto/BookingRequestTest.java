package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestTest {
    private static final Long ITEM_ID = 1L;
    private static final LocalDateTime START = LocalDateTime.of(2024,5,20,10, 30);
    private static final LocalDateTime END = LocalDateTime.of(2024,5,21,12, 50);

    @Test
    void stateIsCorrect() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertEquals(ITEM_ID, request.getItemId());
        assertEquals(START, request.getStart());
        assertEquals(END, request.getEnd());
    }

    @Test
    void equalsTwoObjectsWithSameValues() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertEquals(request, new BookingRequest(ITEM_ID, START, END));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentItemId() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertNotEquals(request, new BookingRequest(2L, START, END));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentStartTD() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertNotEquals(request, new BookingRequest(ITEM_ID,
                LocalDateTime.of(2024,5,21,10, 30), END));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentEndDT() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertNotEquals(request, new BookingRequest(ITEM_ID, START,
                LocalDateTime.of(2024,5,21,10, 30)));
    }

    @Test
    void setItemId() {
        BookingRequest request = new BookingRequest();
        request.setItemId(ITEM_ID);

        assertEquals(ITEM_ID, request.getItemId());
    }

    @Test
    void setStart() {
        BookingRequest request = new BookingRequest();
        request.setStart(START);

        assertEquals(START, request.getStart());
    }

    @Test
    void setEnd() {
        BookingRequest request = new BookingRequest();
        request.setEnd(END);

        assertEquals(END, request.getEnd());
    }

    @Test
    void testToString() {
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);

        assertEquals("BookingRequest(itemId=1, start=2024-05-20T10:30, end=2024-05-21T12:50)",
                request.toString());
    }

    @SneakyThrows
    @Test
    void testDateValidation_isEndDateGreater_whenDatesIsValid_thenReturnTrue() {
        Method method = BookingRequest.class.getDeclaredMethod("isEndDateGreater", null);
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);
        method.setAccessible(true);

        assertEquals(method.invoke(request), true);
    }

    @SneakyThrows
    @Test
    void testDateValidation_isEndDateGreater_whenDatesIsNull_thenReturnTrue() {
        Method method = BookingRequest.class.getDeclaredMethod("isEndDateGreater", null);
        BookingRequest request = new BookingRequest(ITEM_ID, null, null);
        method.setAccessible(true);

        assertEquals(method.invoke(request), true);
    }

    @SneakyThrows
    @Test
    void testDateValidation_isEndDateGreater_whenDatesIsNotValid_thenReturnFalse() {
        Method method = BookingRequest.class.getDeclaredMethod("isEndDateGreater", null);
        BookingRequest request = new BookingRequest(ITEM_ID, END, START);
        method.setAccessible(true);

        assertEquals(method.invoke(request), false);
    }

    @SneakyThrows
    @Test
    void testDateValidation_isStartNotEqualEnd_whenDatesIsValid_thenReturnTrue() {
        Method method = BookingRequest.class.getDeclaredMethod("isStartNotEqualEnd", null);
        BookingRequest request = new BookingRequest(ITEM_ID, START, END);
        method.setAccessible(true);

        assertEquals(method.invoke(request), true);
    }

    @SneakyThrows
    @Test
    void testDateValidation_isStartNotEqualEnd_whenDatesIsNull_thenReturnTrue() {
        Method method = BookingRequest.class.getDeclaredMethod("isStartNotEqualEnd", null);
        BookingRequest request = new BookingRequest(ITEM_ID, null, null);
        method.setAccessible(true);

        assertEquals(method.invoke(request), true);
    }

    @SneakyThrows
    @Test
    void testDateValidation_isStartNotEqualEnd_whenDatesIsNotValid_thenReturnFalse() {
        Method method = BookingRequest.class.getDeclaredMethod("isStartNotEqualEnd", null);
        BookingRequest request = new BookingRequest(ITEM_ID, START, START);
        method.setAccessible(true);

        assertEquals(method.invoke(request), false);
    }
}