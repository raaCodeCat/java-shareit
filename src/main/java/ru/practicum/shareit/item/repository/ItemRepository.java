package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Репозиторий для работы с {@link Item}.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerIdOrderById(Long userId);

    List<Item> findItemsByAvailableIsTrueAndNameContainingIgnoreCaseOrAvailableIsTrueAndDescriptionContainingIgnoreCase(
            String searchText1, String searchText2);
}
