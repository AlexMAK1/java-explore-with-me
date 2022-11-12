package ru.practicum.stat_service.stats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    @NotBlank
    private String uri;
    @NotBlank
    private String app;
    @NotBlank
    private long hits;
}
