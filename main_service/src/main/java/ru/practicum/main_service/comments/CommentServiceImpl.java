package ru.practicum.main_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.EventRepository;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.ValidationException;
import ru.practicum.main_service.user.UserRepository;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public CommentDto create(CommentDto commentDto) {
        if (commentDto == null) {
            throw new ValidationException("Cannot add empty comment.");
        }
        userValidation(commentDto.getCreatorId());
        eventValidation(commentDto.getEventId());
        User user = userRepository.getReferenceById(commentDto.getCreatorId());
        Event event = eventRepository.getReferenceById(commentDto.getEventId());
        LocalDateTime commentDate = LocalDateTime.now();
        Comment comment = CommentConverter.toComment(commentDto, user, event, commentDate);
        log.info("Save new comment: {}", comment);
        return CommentConverter.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Error, validation" +
                " failed. Comment with given id does not exist"));
        log.info("Finding comment with id: {}", id);
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
    public List<CommentDto> getCommentsForEvent(Long eventId, PageRequest pageRequest) {
        eventValidation(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        log.info("Finding all existing comments for event with id: {}, {}", eventId, comments);
        return comments.stream()
                .map(CommentConverter::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getUserComments(Long userId) {
        userValidation(userId);
        List<Comment> comments = commentRepository.findAllByCreatorId(userId);
        log.info("Finding all existing comments for user with id: {}, {}", userId, comments);
        return comments.stream()
                .map(CommentConverter::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto update(CommentDto commentDto, Long userId) {
        userValidation(userId);
        if (commentDto == null) {
            throw new ValidationException("Cannot add empty comment.");
        }
        if (!commentDto.getCreatorId().equals(userId)) {
            log.error("Error, validation failed. Comment does not belong user with id: {}", userId);
            throw new ValidationException("Error, validation failed. Comment does not belong this user");
        }
        Comment comment = commentRepository.findById(commentDto.getCommentId()).orElseThrow(() ->
                new NotFoundException("Comment is not exist"));
        if (!commentDto.getContent().isBlank()) {
            comment.setContent(commentDto.getContent());
        }
        log.info("Updating comment with id: {}, {}", commentDto.getCommentId(), comment);
        return CommentConverter.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void delete(long id, Long userId) {
        userValidation(userId);
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Comment is not exist"));
        log.error("Error, validation failed. Comment with id is not exist: {}", id);
        if (!comment.getCreator().getId().equals(userId)) {
            log.error("Error, validation failed. Comment does not belong user with id: {}", userId);
            throw new ValidationException("Error, validation failed. Comment does not belong this user");
        }
        log.info("Deleting comment with id: {}", id);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteAdmin(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Comment is not exist"));
        log.error("Error, validation failed. Comment with id is not exist: {}", id);
        log.info("Deleting comment with id: {}", id);
        commentRepository.delete(comment);
    }

    private void userValidation(Long userId) {
        if (userId == null) {
            throw new ValidationException("UserId can not be null.");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new ValidationException("User with id = " + userId + " not found.");
        }
    }

    private void eventValidation(Long eventId) {
        if (eventId == null) {
            throw new ValidationException("EventId can not be null.");
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new ValidationException("Event with id = '" + eventId + "'is not exist.");
        }
    }
}
