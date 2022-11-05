package ru.practicum.main_service.compilations;

import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;
import ru.practicum.main_service.compilations.model.Compilation;
import ru.practicum.main_service.events.dto.EventShortDto;
import ru.practicum.main_service.events.model.Event;

import java.util.Set;

public class CompilationConverter {

    public static NewCompilationDto toNewCompilationDto (Compilation compilation, int[] events){
        return new NewCompilationDto(
                compilation.getTitle(),
                compilation.getPinned(),
                events
        );
    }

    public static Compilation toCompilation (NewCompilationDto newCompilationDto, Set<Event> events){
        return new Compilation(
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned(),
                events
        );
    }

    public static CompilationDto toCompilationDto (Compilation compilation, Set<EventShortDto> events){
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                events
        );
    }
}
