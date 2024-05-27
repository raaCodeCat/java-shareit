package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;

/**
 * Репозиторий для работы с {@link Comment}.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c " +
            "from Comment as c " +
            "inner join c.item as i " +
            "where " +
            "i.id in ?1")
    List<Comment> findCommentByIds(List<Long> itemIds);
}
