package ru.practicum.main_service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.events.EventRepository;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.user.UserRepository;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto create(long userId, Long eventId) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Error, validation " +
                "failed. User with given id does not exist"));
        log.error("Error, validation failed. User with given id does not exist: {}", userId);
        Request request = new Request();
        Event event = eventRepository.getReferenceById(eventId);
        request.setCreated(LocalDateTime.now().toString());
        request.setEvent(event);
        request.setRequester(requestor);
        request.setStatus(Status.PENDING);
        requestRepository.save(request);
        log.info("Save the new request: {}", request);
        return RequestConverter.toRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long eventId) {
        log.error("Error, validation failed. User with given id does not exist: {}", userId);
        Event event = eventRepository.getReferenceById(eventId);
        List<Request> requests = requestRepository.findByEvent(event);
        log.info("Finding all requests for event with id: {} {}", eventId, requests);
        return requests.stream()
                .map(RequestConverter::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Request request = requestRepository.getReferenceById(reqId);
        request.setStatus(Status.CONFIRMED);
        requestRepository.save(request);
        return RequestConverter.toRequestDto(request);
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Request request = requestRepository.getReferenceById(reqId);
        request.setStatus(Status.REJECTED);
        requestRepository.save(request);
        return RequestConverter.toRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Error, validation" +
                " failed. User with given id does not exist"));
        log.error("Error, validation failed. User with given id does not exist: {}", userId);
        List<Request> requests = requestRepository.findAllByRequester(requestor);
        log.info("Finding all requests for event for User with id: {} {}", userId, requests);
        return requests.stream()
                .map(RequestConverter::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(long userId, long reqId) {
        Request request = requestRepository.getReferenceById(reqId);
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        return RequestConverter.toRequestDto(request);
    }
}
