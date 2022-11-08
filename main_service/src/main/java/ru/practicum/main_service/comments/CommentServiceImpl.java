package ru.practicum.main_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = CommentConverter.toComment(commentDto);
        log.info("Save new comment: {}", comment);
        return CommentConverter.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Error, validation" +
                " failed. Comment with given id does not exist"));
        return CommentConverter.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(PageRequest pageRequest) {
        Page<Comment> comments = commentRepository.findAll(pageRequest);
        log.info("Finding all existing comments: {}", comments);
        return comments.stream()
                .map(CommentConverter::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsForEvent(long eventId, PageRequest pageRequest) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        log.info("Finding all existing comments for event with id: {}, {}", eventId, comments);
        return comments.stream()
                .map(CommentConverter::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getUserComments(long userId) {
        List<Comment> comments = commentRepository.findAllByCreatorId(userId);
        log.info("Finding all existing comments for user with id: {}, {}", userId, comments);
        return comments.stream()
                .map(CommentConverter::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto update(CommentDto commentDto, long userId) {
        if (commentDto.getCreatorId() != userId) {
            log.error("Error, validation failed. Comment does not belong user with id: {}", userId);
            throw new ValidationException("Error, validation failed. Comment does not belong this user");
        }
        Comment comment = commentRepository.getReferenceById(commentDto.getCommentId());
        if (!commentDto.getContent().isBlank()) {
            comment.setContent(comment.getContent());
        }
        comment.setIsPositive(commentDto.getIsPositive());
        return CommentConverter.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void delete(long id, long userId) {
        Comment comment = commentRepository.getReferenceById(id);
        if (comment.getCreatorId() != userId) {
            log.error("Error, validation failed. Comment does not belong user with id: {}", userId);
            throw new ValidationException("Error, validation failed. Comment does not belong this user");
        }
        commentRepository.delete(comment);
    }
}
