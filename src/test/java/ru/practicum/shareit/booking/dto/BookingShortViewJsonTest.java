package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingShortViewJsonTest {
    private static final Long ID = 1L;
    private static final Long BOOKER_ID = 10L;

    @Autowired
    private JacksonTester<BookingShortView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        BookingShortView view = new BookingShortView(ID, BOOKER_ID);

        JsonContent<BookingShortView> actual = json.write(view);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.bookerId");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathNumberValue("$.bookerId").isEqualTo(10);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        BookingShortView actual = json.parseObject("{" +
                "\"id\":1," +
                "\"bookerId\":10" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getBookerId()).isEqualTo(BOOKER_ID);
    }

}