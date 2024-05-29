package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingShortViewTest {
    private static final Long ID = 11L;
    private static final Long BOOKER_ID = 2L;

    @Test
    void stateIsCorrect() {
        BookingShortView bookingShortView = new BookingShortView(ID, BOOKER_ID);

        assertEquals(ID, bookingShortView.getId());
        assertEquals(BOOKER_ID, bookingShortView.getBookerId());
    }

    @Test
    void equalsTwoObjectsWithSameValues() {
        BookingShortView bookingShortView = new BookingShortView(ID, BOOKER_ID);

        assertEquals(bookingShortView, new BookingShortView(ID, BOOKER_ID));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentId() {
        BookingShortView bookingShortView = new BookingShortView(ID, BOOKER_ID);

        assertNotEquals(bookingShortView, new BookingShortView(100L, BOOKER_ID));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentBookerId() {
        BookingShortView bookingShortView = new BookingShortView(ID, BOOKER_ID);

        assertNotEquals(bookingShortView, new BookingShortView(ID, 100L));
    }

    @Test
    void setId() {
        BookingShortView bookingShortView = new BookingShortView();
        bookingShortView.setId(ID);

        assertEquals(ID, bookingShortView.getId());
    }

    @Test
    void setBookerId() {
        BookingShortView bookingShortView = new BookingShortView();
        bookingShortView.setBookerId(BOOKER_ID);

        assertEquals(BOOKER_ID, bookingShortView.getBookerId());
    }

    @Test
    void testToString() {
        BookingShortView bookingShortView = new BookingShortView(ID, BOOKER_ID);

        assertEquals("BookingShortView(id=11, bookerId=2)", bookingShortView.toString());
    }
}