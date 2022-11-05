package ru.practicum.main_service.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    private long id;
    @NotBlank
    private String uri;
    @NotBlank
    private String app;
    @NotBlank
    private String ip;
    private String timestamp;
}
