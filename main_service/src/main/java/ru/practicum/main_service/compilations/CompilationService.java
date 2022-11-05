package ru.practicum.main_service.compilations;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto getCompilation(long id);

    List<CompilationDto> getCompilations(PageRequest pageRequest);

    void addEventToCompilation(long compId, long eventId);

    void deleteEvent (long compId, long eventId);

    void unpinCompilation(long compId);

    void pinCompilation(long compId);

    void deleteCompilation(long compId);
}
