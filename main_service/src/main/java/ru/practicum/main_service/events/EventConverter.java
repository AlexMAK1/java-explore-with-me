package ru.practicum.main_service.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.dto.NewEventDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConverter {

    public static Event toEvent(NewEventDto newEventDto, Category category, String createdOn, LocalDateTime eventDate, User initiator) {
        return new Event(
                newEventDto.getId(),
                newEventDto.getAnnotation(),
                category,
                0L,
                createdOn,
                newEventDto.getDescription(),
                eventDate,
                initiator,
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                "yyyy-MM-dd HH:mm:ss",
                newEventDto.getRequestModeration(),
                State.PENDING,
                newEventDto.getTitle(),
                0L);
    }

    public static EventFullDto toEventFullDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {

        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().format(EventServiceImpl.formatter),
                userShortDto,
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventShortDto toEventShortDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                event.getConfirmedRequests(),
                event.getEventDate().toString(),
                userShortDto,
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}

