package ru.practicum.main_service.compilations.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    @NotBlank
    private String title;
    private Boolean pinned;
    private Set<EventShortDto> events = new HashSet<>();
}
