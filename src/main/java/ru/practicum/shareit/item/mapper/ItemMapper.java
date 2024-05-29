package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseView;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemView toItemView(Item source) {
        return new ItemView(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable(), null, null, new ArrayList<>(),
                source.getRequest() != null ? source.getRequest().getId() : null);
    }

    public static List<ItemView> toItemView(List<Item> source) {
        return source.stream()
                .map(ItemMapper::toItemView)
                .collect(Collectors.toList());
    }

    public static Item fromItemCreateUpdateDto(ItemCreateUpdateDto source) {
        return new Item(null, source.getName(), source.getDescription(),
                source.getAvailable(), null, null);
    }

    public static ItemResponseView toItemResponseView(Item source) {
        return new ItemResponseView(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable(), source.getRequest() != null ? source.getRequest().getId() : null);
    }

    public static List<ItemResponseView> toItemResponseView(List<Item> source) {
        return source.stream()
                .map(ItemMapper::toItemResponseView)
                .collect(Collectors.toList());
    }
}
