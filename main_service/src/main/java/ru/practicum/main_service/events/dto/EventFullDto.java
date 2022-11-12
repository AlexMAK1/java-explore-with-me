package ru.practicum.main_service.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.events.State;
import ru.practicum.main_service.events.model.Location;
import ru.practicum.main_service.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;
    @NotBlank
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    @NotBlank
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private String  publishedOn;
    private Boolean requestModeration;
    private State state;
    @NotBlank
    private String title;
    private Long views;
}
