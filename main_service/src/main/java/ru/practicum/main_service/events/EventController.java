package ru.practicum.main_service.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.NewEventDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto create(@Valid @RequestBody NewEventDto newEventDto, @PathVariable("userId") long userId) {
        return eventService.create(newEventDto, userId);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publicEvent(@PathVariable("eventId") long eventId) {
        return eventService.publicEvent(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable("eventId") long eventId) {
        return eventService.rejectEvent(eventId);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateAdminEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable("eventId") long eventId) {
        return eventService.updateAdminEvent(eventId, newEventDto);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getCategory(@PathVariable("id") long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelUserEvent(userId, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventFullDto> getUserEvents(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size,
                                            @PathVariable Long userId) {
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return eventService.getUserEvents(userId, pageRequest);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateUserEvent(@Valid @RequestBody NewEventDto newEventDto,
                                        @PathVariable("userId") long userId) {
        return eventService.updateUserEvent(userId, newEventDto);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                        @RequestParam(name = "paid", required = false) Boolean paid,
                                        @RequestParam(name = "rangeStart") String rangeStart,
                                        @RequestParam(name = "rangeEnd") String rangeEnd,
                                        @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                                        @RequestParam(name = "sort", required = false) String sort,
                                        HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStartTime = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime rangeEndTime = LocalDateTime.parse(rangeEnd, formatter);
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        return eventService.getEvents(text, categories, paid, rangeStartTime, rangeEndTime, onlyAvailable, pageRequest, request);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) List<String> states,
                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0")  int from,
                                        @RequestParam(name = "size", defaultValue = "10")  int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStartTime = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime rangeEndTime = LocalDateTime.parse(rangeEnd, formatter);
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return eventService.getAdminEvents(users, states, categories, rangeStartTime, rangeEndTime, pageRequest);
    }
}
