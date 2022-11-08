package ru.practicum.main_service.comments;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.comments.dto.CommentDto;
import ru.practicum.main_service.comments.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConverter {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getIsPositive(),
                comment.getCreatorId(),
                comment.getEventId()
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getCommentId(),
                commentDto.getContent(),
                commentDto.getIsPositive(),
                commentDto.getCreatorId(),
                commentDto.getEventId()
        );
    }

}
