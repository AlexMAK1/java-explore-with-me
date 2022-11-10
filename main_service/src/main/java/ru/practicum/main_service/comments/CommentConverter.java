package ru.practicum.main_service.comments;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConverter {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreator().getId(),
                comment.getEvent().getId()
        );
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return new Comment(
                commentDto.getCommentId(),
                commentDto.getContent(),
                user,
                event
        );
    }
}
