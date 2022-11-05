package ru.practicum.main_service.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.request.Status;

import javax.validation.constraints.NotBlank;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    private Long event;
    private String created;
    @NotBlank
    private Status status;
    private Long requester;
}
