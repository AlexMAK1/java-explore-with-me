package ru.practicum.main_service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;


    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") long userId, @PathVariable("eventId")
    long eventId) {
        return requestService.getRequests(userId, eventId);
    }

    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDto create(@PathVariable("userId") long userId, @RequestParam(name = "eventId")
    Long eventId) {
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable("userId") long userId, @PathVariable("eventId")
    long eventId, @PathVariable("reqId") long reqId) {
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable("userId") long userId, @PathVariable("eventId")
    long eventId, @PathVariable("reqId") long reqId) {
        return requestService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable("userId") long userId) {
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/users/{userId}/requests/{reqId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable("userId") long userId, @PathVariable("reqId") long reqId) {
        return requestService.cancelUserRequest(userId, reqId);
    }
}
