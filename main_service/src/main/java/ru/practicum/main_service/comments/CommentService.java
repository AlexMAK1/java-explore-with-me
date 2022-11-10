package ru.practicum.main_service.comments;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.comments.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(CommentDto commentDto);

    CommentDto getComment(long id);

    List<CommentDto> getComments(PageRequest pageRequest);

    List<CommentDto> getCommentsForEvent(Long eventId, PageRequest pageRequest);

    List<CommentDto> getUserComments(Long userId);

    CommentDto update(CommentDto commentDto, Long userId);

    void delete(long id, Long userId);
}
