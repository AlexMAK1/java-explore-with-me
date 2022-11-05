package ru.practicum.main_service.events;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.events.dto.EventFullDto;
import ru.practicum.main_service.events.dto.NewEventDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto create(NewEventDto newEventDto, Long userId);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventFullDto> getUserEvents(Long userId, PageRequest pageRequest);

    EventFullDto updateUserEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateAdminEvent(Long eventId, NewEventDto newEventDto);

    EventFullDto publicEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    List<EventFullDto> getEvents(String text,
                                 List<Long> categories,
                                 Boolean paid,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Boolean onlyAvailable,
                                 PageRequest pageRequest,
                                 HttpServletRequest request);

    List<EventFullDto> getAdminEvents(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStartTime,
                                      LocalDateTime rangeEndTime,
                                      PageRequest pageRequest);
}
