package ru.practicum.main_service.request;

import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto create(long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId);

    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto cancelUserRequest(long userId, long reqId);
}
