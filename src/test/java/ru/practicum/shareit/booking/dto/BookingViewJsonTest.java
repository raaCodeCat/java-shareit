package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.shareitutils.dto.IdFieldView;
import ru.practicum.shareit.shareitutils.dto.IdNameFieldView;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingViewJsonTest {
    private static final Long ID = 1L;
    private static final LocalDateTime START = LocalDateTime.of(2024,5, 30, 10, 30, 30);
    private static final LocalDateTime END = LocalDateTime.of(2024,5, 31, 10, 30, 30);
    private static final BookingStatus STATUS = BookingStatus.APPROVED;
    private static final IdFieldView BOOKER = new IdFieldView(111L);
    private static final IdNameFieldView ITEM = new IdNameFieldView(22L, "Item name");

    @Autowired
    private JacksonTester<BookingView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        BookingView view = new BookingView(ID, START, END, STATUS, BOOKER, ITEM);

        JsonContent<BookingView> actual = json.write(view);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.start");
        assertThat(actual).hasJsonPath("$.end");
        assertThat(actual).hasJsonPath("$.status");
        assertThat(actual).hasJsonPath("$.booker");
        assertThat(actual).hasJsonPath("$.item");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathValue("$.start").isEqualTo(START.toString());
        assertThat(actual).extractingJsonPathValue("$.end").isEqualTo(END.toString());
        assertThat(actual).extractingJsonPathValue("$.status").isEqualTo(STATUS.toString());
        assertThat(actual).extractingJsonPathNumberValue("$.booker.id").isEqualTo(111);
        assertThat(actual).extractingJsonPathNumberValue("$.item.id").isEqualTo(22);
        assertThat(actual).extractingJsonPathStringValue("$.item.name").isEqualTo("Item name");
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        BookingView actual = json.parseObject("{" +
                "\"id\":1," +
                "\"start\":\"2024-05-30T10:30:30\"," +
                "\"end\":\"2024-05-31T10:30:30\"," +
                "\"status\":\"APPROVED\"," +
                "\"booker\":{" +
                "\"id\":111" +
                "}," +
                "\"item\":{" +
                "\"id\":22," +
                " \"name\":\"Item name\"" +
                "}" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getStart()).isEqualTo(START);
        assertThat(actual.getEnd()).isEqualTo(END);
        assertThat(actual.getStatus()).isEqualTo(STATUS);
        assertThat(actual.getBooker()).isEqualTo(BOOKER);
        assertThat(actual.getItem()).isEqualTo(ITEM);
    }
}