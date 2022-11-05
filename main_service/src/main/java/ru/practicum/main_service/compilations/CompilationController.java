package ru.practicum.main_service.compilations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.compilations.dto.CompilationDto;
import ru.practicum.main_service.compilations.dto.NewCompilationDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping("/admin/compilations")
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.create(newCompilationDto);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@Valid @PathVariable("compId") long compId) {
        return compilationService.getCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable("compId") @Valid long compId, @PathVariable long eventId) {
        compilationService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEvent(@PathVariable("compId") @Valid long compId, @PathVariable long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable("compId") @Valid long compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable("compId") @Valid long compId) {
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable("compId") @Valid long compId) {
        compilationService.deleteCompilation(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findAll(@RequestParam(name = "from", defaultValue = "0")
                                     Integer from, @RequestParam(name = "size",
            defaultValue = "10") Integer size) {
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return compilationService.getCompilations(pageRequest);
    }
}
