package ru.practicum.stat_service.stats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stat_service.stats.dto.EndpointHit;
import ru.practicum.stat_service.stats.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsConverter {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(EndpointHit endpointHit) {
        return new Hit(endpointHit.getId(),
                endpointHit.getUri(),
                endpointHit.getApp(),
                endpointHit.getIp(),
                LocalDateTime.parse(endpointHit.getTimestamp(), formatter));
    }
}
