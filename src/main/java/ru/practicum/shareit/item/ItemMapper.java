package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item source) {
        return new ItemDto(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable());
    }

    public static List<ItemDto> toItemDto(List<Item> source) {
        return source.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public static Item fromItemDto(ItemDto source) {
        return new Item(source.getId(), source.getName(), source.getDescription(),
                source.getAvailable(), null);
    }
}
