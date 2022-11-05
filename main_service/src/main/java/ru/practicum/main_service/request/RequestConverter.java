package ru.practicum.main_service.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestConverter {

    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getCreated(),
                request.getStatus(),
                request.getRequester().getId());
    }

    public static Request toRequest(ParticipationRequestDto participationRequestDto, User requestor, Event event) {
        return new Request(
                participationRequestDto.getId(),
                event,
                participationRequestDto.getCreated(),
                participationRequestDto.getStatus(),
                requestor);
    }
}
