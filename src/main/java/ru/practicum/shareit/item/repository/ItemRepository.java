package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Репозиторий для работы с {@link Item}.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerIdOrderById(Long userId);

    @Query("select i " +
            "from Item as i " +
            "where " +
            "i.available = true " +
            "and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> findItemBySearchText(String searchText);
}
