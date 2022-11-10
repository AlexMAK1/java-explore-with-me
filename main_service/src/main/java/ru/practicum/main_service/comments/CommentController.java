package ru.practicum.main_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comments.dto.CommentDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/comments")
    public CommentDto create(@Valid @RequestBody CommentDto commentDto) {
        return commentService.create(commentDto);
    }

    @GetMapping("/comments/{id}")
    public CommentDto getComment(@PathVariable("id") long id) {
        return commentService.getComment(id);
    }

    @GetMapping("/comments")
    public List<CommentDto> getComments(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return commentService.getComments(pageRequest);
    }

    @GetMapping("/comments/{eventId}")
    public List<CommentDto> getCommentsForEvent(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @PathVariable("eventId") long eventId) {
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return commentService.getCommentsForEvent(eventId, pageRequest);
    }

    @GetMapping("/users/comments/{userId}")
    public List<CommentDto> getUserComments(@PathVariable("userId") long userId) {
        return commentService.getUserComments(userId);
    }

    @PutMapping("/users/comments/{userId}")
    public CommentDto updateComment(@RequestBody CommentDto commentDto, @PathVariable("userId") long userId) {
        return commentService.update(commentDto, userId);
    }

    @DeleteMapping("/users/comments/{id}")
    public void deleteComment(@PathVariable("id") long id, @RequestParam(name = "userId") long userId) {
        commentService.delete(id, userId);
    }
}
