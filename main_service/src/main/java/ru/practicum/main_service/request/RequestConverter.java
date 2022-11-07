package ru.practicum.main_service.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.model.Request;

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
}
