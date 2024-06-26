package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestViewJsonTest {
    private static final Long ID = 1L;
    private static final String DESCRIPTION = "This is description";
    private static final LocalDateTime CREATED = LocalDateTime.of(2024,5, 30, 10, 30, 30);

    @Autowired
    private JacksonTester<ItemRequestView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemRequestView itemRequestView = new ItemRequestView(ID, DESCRIPTION, CREATED);

        JsonContent<ItemRequestView> actual = json.write(itemRequestView);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.created");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathStringValue("$.description").isEqualTo(DESCRIPTION);
        assertThat(actual).extractingJsonPathValue("$.created").isEqualTo(CREATED.toString());
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        ItemRequestView actual = json.parseObject("{" +
                "\"id\":1," +
                "\"description\":\"This is description\"," +
                "\"created\":\"2024-05-30T10:30:30\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getCreated()).isEqualTo(CREATED);
    }

}