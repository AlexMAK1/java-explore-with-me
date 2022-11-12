package ru.practicum.main_service.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAll(Pageable pageable);

    List<Comment> findAllByCreatorId(Long userId);

    List<Comment> findAllByEventId(Long eventId);

}
