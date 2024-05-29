package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortView;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для ответа {@link Item}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemView {
    /**
     * Идентификатор вещи.
     */
    private Long id;

    /**
     * Краткое название.
     */
    private String name;

    /**
     * Развёрнутое описание.
     */
    private String description;

    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    private Boolean available;

    /**
     * Последнее бронирование;
     */
    private BookingShortView lastBooking;

    /**
     * Ближайшее следующее бронирование;
     */
    private BookingShortView nextBooking;

    /**
     * Отзывы.
     */
    private List<CommentView> comments = new ArrayList<>();

    /**
     * Идентификатор запроса.
     */
    private Long requestId;
}

