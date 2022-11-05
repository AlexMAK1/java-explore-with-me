package ru.practicum.main_service.compilations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.CategoryConverter;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.events.EventConverter;
import ru.practicum.main_service.events.EventRepository;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.UserConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        for (long id : newCompilationDto.getEvents()) {
            Event event = eventRepository.getReferenceById(id);
            events.add(event);
        }
        Compilation compilation = CompilationConverter.toCompilation(newCompilationDto, events);
        log.info("Save new compilation: {}", compilation);
        Set<EventShortDto> eventShortDtos = new HashSet<>();
        for (Event event : events) {
            EventShortDto eventShortDto = EventConverter.toEventShortDto(event,
                    CategoryConverter.toCategoryDto(event.getCategory()),
                    UserConverter.toUserShortDto(event.getInitiator()));
            eventShortDtos.add(eventShortDto);
        }
        return CompilationConverter.toCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    public CompilationDto getCompilation(long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Error, " +
                "validation failed. Compilation with given id does not exist"));
        log.info("Find compilation with id: {} {}", id, compilation);
        Set<EventShortDto> eventShortDtos = new HashSet<>();
        for (Event event : compilation.getEvents()) {
            EventShortDto eventShortDto = EventConverter.toEventShortDto(event,
                    CategoryConverter.toCategoryDto(event.getCategory()),
                    UserConverter.toUserShortDto(event.getInitiator()));
            eventShortDtos.add(eventShortDto);
        }
        return CompilationConverter.toCompilationDto(compilation, eventShortDtos);
    }

    @Override
    public List<CompilationDto> getCompilations(PageRequest pageRequest) {
        Page<Compilation> compilations = compilationRepository.findAll(pageRequest);
        log.info("Find all existing compilations: {}", compilations);
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            Set<EventShortDto> eventShortDtos = new HashSet<>();
            compilationDtoList.add(CompilationConverter.toCompilationDto(compilation, eventShortDtos));
            for (Event event : compilation.getEvents()) {
                EventShortDto eventShortDto = EventConverter.toEventShortDto(event,
                        CategoryConverter.toCategoryDto(event.getCategory()),
                        UserConverter.toUserShortDto(event.getInitiator()));
                eventShortDtos.add(eventShortDto);
            }
        }
        return compilationDtoList;
    }


    @Override
    public void addEventToCompilation(long compId, long eventId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        Event event = eventRepository.getReferenceById(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteEvent(long compId, long eventId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        Event event = eventRepository.getReferenceById(eventId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilationRepository.delete(compilation);
    }
}
