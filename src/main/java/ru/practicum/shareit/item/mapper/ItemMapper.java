package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemView toItemView(Item source) {
        return new ItemView(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable(), null, null, new ArrayList<>());
    }

    public static List<ItemView> toItemView(List<Item> source) {
        return source.stream()
                .map(ItemMapper::toItemView)
                .collect(Collectors.toList());
    }

    public static Item fromItemRequest(ItemRequest source) {
        return new Item(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable(), null);
    }
}
