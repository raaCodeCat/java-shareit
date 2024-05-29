package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с {@link ItemRequest}.
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select distinct r " +
            "from ItemRequest as r " +
            "inner join r.user as u " +
            "left join fetch r.items as i " +
            "where " +
            "u.id = ?1 " +
            "order by r.created desc")
    List<ItemRequest> findItemRequestsByUserId(Long userId);

    @Query("select distinct r " +
            "from ItemRequest as r " +
            "left join fetch r.items as i " +
            "where " +
            "r.id = ?1")
    Optional<ItemRequest> findItemRequestsById(Long itemRequestId);

    @Query("select distinct r " +
            "from ItemRequest as r " +
            "inner join r.user as u " +
            "left join fetch r.items as i " +
            "where " +
            "u.id != ?1 " +
            "order by r.created desc")
    List<ItemRequest> findItemRequestsExcludingOwnPageable(Long userId, Pageable pageable);
}
