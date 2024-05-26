package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    Optional<Booking> findFirstByItemIdAndStartDtBeforeAndStatusIsNotOrderByEndDtDescIdAsc(
            Long itemId,LocalDateTime dt, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartDtAfterAndStatusIsNotOrderByStartDtAscIdAsc(
            Long itemId, LocalDateTime dt, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndBookerIdAndEndDtBeforeAndStatusIsNot(
            Long itemId, Long userId, LocalDateTime dt, BookingStatus status);
}
