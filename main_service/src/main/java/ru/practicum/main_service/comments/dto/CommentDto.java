package ru.practicum.main_service.comments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    @NotBlank
    private String content;
    @NotNull
    private Long creatorId;
    @NotNull
    private Long eventId;
    @NotNull
    private String commentDate;
}
