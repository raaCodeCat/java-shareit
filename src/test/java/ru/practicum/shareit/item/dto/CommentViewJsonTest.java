package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentViewJsonTest {
    private static final Long ID = 1L;
    private static final String TEXT = "text";
    private static final String AUTHOR_NAME = "Author name";
    private static final LocalDateTime CREATED = LocalDateTime.of(2024,5, 30, 10, 30, 30);

    @Autowired
    private JacksonTester<CommentView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        CommentView commentView = new CommentView(ID, TEXT, AUTHOR_NAME, CREATED);

        JsonContent<CommentView> actual = json.write(commentView);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.text");
        assertThat(actual).hasJsonPath("$.authorName");
        assertThat(actual).hasJsonPath("$.created");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathStringValue("$.text").isEqualTo(TEXT);
        assertThat(actual).extractingJsonPathStringValue("$.authorName").isEqualTo(AUTHOR_NAME);
        assertThat(actual).extractingJsonPathValue("$.created").isEqualTo(CREATED.toString());
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        CommentView actual = json.parseObject("{" +
                "\"id\":1," +
                "\"text\":\"text\"," +
                "\"authorName\":\"Author name\"," +
                "\"created\":\"2024-05-30T10:30:30\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getText()).isEqualTo(TEXT);
        assertThat(actual.getAuthorName()).isEqualTo(AUTHOR_NAME);
        assertThat(actual.getCreated()).isEqualTo(CREATED);
    }
}