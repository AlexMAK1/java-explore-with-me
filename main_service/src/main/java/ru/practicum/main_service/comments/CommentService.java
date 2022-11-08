package ru.practicum.main_service.comments;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.comments.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(CommentDto commentDto);

    CommentDto getComment(long id);

    List<CommentDto> getComments(PageRequest pageRequest);

    List<CommentDto> getCommentsForEvent(long eventId, PageRequest pageRequest);

    List<CommentDto> getUserComments(long userId);

    CommentDto update(CommentDto commentDto, long userId);

    void delete(long id, long userId);
}
