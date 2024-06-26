package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestJsonTest {
    private static final Long ITEM_ID = 1L;
    private static final LocalDateTime START = LocalDateTime.of(2024,5, 30, 10, 30, 30);
    private static final LocalDateTime END = LocalDateTime.of(2024,5, 31, 10, 30, 30);

    @Autowired
    private JacksonTester<BookingRequest> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        BookingRequest bookingRequest = new BookingRequest(ITEM_ID,START, END);

        JsonContent<BookingRequest> actual = json.write(bookingRequest);

        assertThat(actual).hasJsonPath("$.itemId");
        assertThat(actual).hasJsonPath("$.start");
        assertThat(actual).hasJsonPath("$.end");
        assertThat(actual).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(actual).extractingJsonPathValue("$.start").isEqualTo(START.toString());
        assertThat(actual).extractingJsonPathValue("$.end").isEqualTo(END.toString());
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        BookingRequest actual = json.parseObject("{" +
                "\"itemId\":1," +
                "\"start\":\"2024-05-30T10:30:30\"," +
                "\"end\":\"2024-05-31T10:30:30\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getItemId()).isEqualTo(ITEM_ID);
        assertThat(actual.getStart()).isEqualTo(START);
        assertThat(actual.getEnd()).isEqualTo(END);
    }
}