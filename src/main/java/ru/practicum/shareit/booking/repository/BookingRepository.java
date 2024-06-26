package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b " +
            "from Booking as b " +
            "inner join b.booker as u " +
            "where " +
            "u.id = ?1 " +
            "and (" +
            "?2 = 'ALL'" +
            "or ?2 = 'CURRENT' and current_timestamp() between b.startDt and b.endDt " +
            "or ?2 = 'PAST' and b.endDt < current_timestamp() " +
            "or ?2 = 'FUTURE' and b.startDt > current_timestamp() " +
            "or ?2 = 'WAITING' and b.status = 'WAITING' " +
            "or ?2 = 'REJECTED' and b.status = 'REJECTED' " +
            ") " +
            "order by b.startDt desc")
    List<Booking> getBookingsByBooker(Long userId, String state);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.booker as u " +
            "where " +
            "u.id = ?1 " +
            "and (" +
            "?2 = 'ALL'" +
            "or ?2 = 'CURRENT' and current_timestamp() between b.startDt and b.endDt " +
            "or ?2 = 'PAST' and b.endDt < current_timestamp() " +
            "or ?2 = 'FUTURE' and b.startDt > current_timestamp() " +
            "or ?2 = 'WAITING' and b.status = 'WAITING' " +
            "or ?2 = 'REJECTED' and b.status = 'REJECTED' " +
            ") " +
            "order by b.startDt desc")
    List<Booking> getBookingsByBookerPageable(Long userId, String state, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.item as i " +
            "inner join i.owner as o " +
            "where " +
            "o.id = ?1 " +
            "and (" +
            "?2 = 'ALL'" +
            "or ?2 = 'CURRENT' and current_timestamp() between b.startDt and b.endDt " +
            "or ?2 = 'PAST' and b.endDt < current_timestamp() " +
            "or ?2 = 'FUTURE' and b.startDt > current_timestamp() " +
            "or ?2 = 'WAITING' and b.status = 'WAITING' " +
            "or ?2 = 'REJECTED' and b.status = 'REJECTED' " +
            ") " +
            "order by b.startDt desc")
    List<Booking> getBookingsByOwner(Long userId, String state);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.item as i " +
            "inner join i.owner as o " +
            "where " +
            "o.id = ?1 " +
            "and (" +
            "?2 = 'ALL'" +
            "or ?2 = 'CURRENT' and current_timestamp() between b.startDt and b.endDt " +
            "or ?2 = 'PAST' and b.endDt < current_timestamp() " +
            "or ?2 = 'FUTURE' and b.startDt > current_timestamp() " +
            "or ?2 = 'WAITING' and b.status = 'WAITING' " +
            "or ?2 = 'REJECTED' and b.status = 'REJECTED' " +
            ") " +
            "order by b.startDt desc")
    List<Booking> getBookingsByOwnerPageable(Long userId, String state, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.item as i " +
            "where " +
            "i.id in (?1) " +
            "and b.startDt < ?2 " +
            "and b.status != 'REJECTED'")
    List<Booking> findLastBookingByItemIds(
            List<Long> itemIds, LocalDateTime dt);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.item as i " +
            "where " +
            "i.id in (?1) " +
            "and b.startDt > ?2 " +
            "and b.status != 'REJECTED'")
    List<Booking> findNextBookingByItemIds(
            List<Long> itemIds, LocalDateTime dt);

    @Query("select b " +
            "from Booking as b " +
            "inner join b.item as i " +
            "inner join b.booker as u " +
            "where " +
            "i.id = ?1 " +
            "and u.id = ?2 " +
            "and b.endDt < ?3 " +
            "and b.status != 'REJECTED' " +
            "and b.status != 'WAITING'")
    List<Booking> findBookingForAllowCommentPageable(
            Long itemId, Long userId, LocalDateTime dt, Pageable pageable);
}
