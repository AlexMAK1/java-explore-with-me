package ru.practicum.main_service.events;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.CategoryConverter;
import ru.practicum.main_service.categories.CategoryRepository;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.events.dto.*;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.ValidationException;
import ru.practicum.main_service.user.UserConverter;
import ru.practicum.main_service.user.UserRepository;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.main_service.events.model.QEvent.event;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Lazy
    private final StatClient statClient;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String startTime = LocalDateTime.now().minusDays(15).format(formatter);
    private final String endTime = LocalDateTime.now().plusDays(15).format(formatter);
    private final boolean unique = false;

    @Override
    public EventFullDto create(NewEventDto newEventDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventTime = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        if (now.isAfter(eventTime)) {
            log.error("Error, validation failed. Event time cannot be earlier than two hours from now: {}",
                    newEventDto.getEventDate());
            throw new ValidationException("Error, validation failed. Event time cannot be earlier than two hours " +
                    "from now");
        }
        Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
        User initiator = userRepository.getReferenceById(userId);
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        Event event = EventConverter.toEvent(newEventDto, category, now.format(formatter), eventDate, initiator);
        log.info("Save new event: {}", event);
        eventRepository.save(event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        log.info("Get event with id: {}, {}", eventId, event);
        statClient.createHit(new EndpointHit(0,
                request.getRequestURI(),
                "main_service",
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)));
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        String uri = "/events/" + event.getId();
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        List<ViewStats> views = getViewStats(startTime, endTime, uris, unique);
        EventFullDto eventFullDto = EventConverter.toEventFullDto(event, categoryDto, userShortDto);
        eventFullDto.setViews(views.get(0).getHits());
        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getUserEvents(Long userId, PageRequest pageRequest) {
        User user = userRepository.getReferenceById(userId);
        Page<Event> events = eventRepository.findAllByInitiator(user, pageRequest);
        log.info("Get events for User with id: {}, {}", userId, events);
        return getEventFullDtos(events);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.findByInitiator(user);
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getCategory() != null) {
            Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
            event.setCategory(category);
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        }
        if (newEventDto.getLocation() != null) {
            event.setLocation(newEventDto.getLocation());
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
        eventRepository.save(event);
        log.info("Update event with id: {}, {}", event.getId(), event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, NewEventDto newEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getCategory() != null) {
            Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
            event.setCategory(category);
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        }
        if (newEventDto.getLocation() != null) {
            event.setLocation(newEventDto.getLocation());
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
        eventRepository.save(event);
        log.info("Update event with id: {}, {}", event.getId(), event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public EventFullDto publicEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        LocalDateTime publishedOn = LocalDateTime.now();
        if (publishedOn.isAfter(event.getEventDate()) || !event.getState().equals(State.PENDING)) {
            log.error("Error, validation failed. Event time cannot be earlier than two hours from now or the event " +
                    "must be in a  pending state: {}, {}", event.getEventDate(), event.getState());
            throw new ValidationException("Error, validation failed. Event time cannot be earlier than two hours " +
                    "or the event must be in a  pending state from now");
        }
        event.setPublishedOn(publishedOn.format(formatter));
        event.setState(State.PUBLISHED);
        log.info("Public event with id: {}, {}", event.getId(), event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        LocalDateTime publishedOn = LocalDateTime.now();

        if (publishedOn.isAfter(event.getEventDate()) || !event.getState().equals(State.PENDING)) {
            log.error("Error, validation failed. Event time cannot be earlier than two hours from now or the event " +
                    "must be in a  pending state: {}, {}", event.getEventDate(), event.getState());
            throw new ValidationException("Error, validation failed. Event time cannot be earlier than two hours " +
                    "or the event must be in a  pending state from now");
        }
        event.setPublishedOn(publishedOn.format(formatter));
        event.setState(State.CANCELED);
        log.info("Cancel event with id: {}, {}", event.getId(), event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);

    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        log.info("Get event with id: {}, {}", eventId, event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Events with this id " +
                "does not exist"));
        log.info("Cancel event with id: {}, {}", eventId, event);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
        UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
        event.setState(State.CANCELED);
        eventRepository.save(event);
        return EventConverter.toEventFullDto(event, categoryDto, userShortDto);
    }

    @Override
    public List<EventFullDto> getEvents(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        PageRequest pageRequest,
                                        HttpServletRequest request) {

        EventFilter filter = EventFilter.builder()
                .annotation(text)
                .description(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .build();

        Predicate predicate = QPredicates.builder()
                .add(filter.getAnnotation(), event.annotation::containsIgnoreCase)
                .add(filter.getCategories(), event.category.id::in)
                .add(filter.getPaid(), event.paid::eq)
                .add(filter.getRangeStart(), event.eventDate::after)
                .add(filter.getRangeEnd(), event.eventDate::before)
                .buildAnd();

        Page<Event> events = eventRepository.findAll(predicate, pageRequest);

        statClient.createHit(new EndpointHit(0,
                request.getRequestURI(),
                "main_service",
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)));

        log.info("Get events : {}", events);
        return getEventFullDtos(events);
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users,
                                             List<String> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStartTime,
                                             LocalDateTime rangeEndTime,
                                             PageRequest pageRequest) {
        EventFilter filter = EventFilter.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStartTime)
                .rangeEnd(rangeEndTime)
                .build();

        Predicate predicate = QPredicates.builder()
                .add(filter.getUsers(), event.initiator.id::in)
                .add(filter.getCategories(), event.category.id::in)
                .add(filter.getRangeStart(), event.eventDate::after)
                .add(filter.getRangeEnd(), event.eventDate::before)
                .buildAnd();

        Page<Event> events = eventRepository.findAll(predicate, pageRequest);
        log.info("Get events : {}", events);
        return getEventFullDtos(events);
    }

    private List<ViewStats> getViewStats(String startTime, String endTime, List<String> uris, boolean unique) {
        ResponseEntity<Object> response = statClient.getStats(startTime, endTime, uris, unique);
        List<ViewStats> views = new ArrayList<>();
        List<Map<String, Object>> stats = (List<Map<String, Object>>) response.getBody();
        for (Map<String, Object> s : stats) {
            ViewStats viewStats = new ViewStats(s.get("uri").toString(),
                    s.get("app").toString(),
                    ((Number) s.get("hits")).longValue());
            views.add(viewStats);
            log.info("Get views : {}", views);
        }
        return views;
    }

    private List<EventFullDto> getEventFullDtos(Page<Event> eventList) {
        List<String> uris = new ArrayList<>();
        Map<String, EventFullDto> eventFullDtoMap = new HashMap<>();
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : eventList) {
            CategoryDto categoryDto = CategoryConverter.toCategoryDto(event.getCategory());
            UserShortDto userShortDto = UserConverter.toUserShortDto(event.getInitiator());
            EventFullDto eventFullDto = EventConverter.toEventFullDto(event, categoryDto, userShortDto);
            eventFullDtos.add(eventFullDto);
        }
        for (EventFullDto eventFullDto : eventFullDtos) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/events/").append(eventFullDto.getId());
            uris.add(String.valueOf(stringBuilder));
            eventFullDtoMap.put(String.valueOf(stringBuilder), eventFullDto);
        }
        List<ViewStats> viewStatsList = getViewStats(startTime, endTime, uris, unique);
        for (ViewStats viewStats : viewStatsList) {
            EventFullDto eventFullDto = eventFullDtoMap.get(viewStats.getUri());
            eventFullDto.setViews(viewStats.getHits());
            eventFullDtoMap.put(viewStats.getUri(), eventFullDto);
        }
        eventFullDtos.clear();
        eventFullDtos.addAll(eventFullDtoMap.values());

        return eventFullDtos;
    }
}
