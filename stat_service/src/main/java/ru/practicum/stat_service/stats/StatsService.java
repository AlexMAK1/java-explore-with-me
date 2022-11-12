package ru.practicum.stat_service.stats;

import ru.practicum.stat_service.stats.dto.EndpointHit;
import ru.practicum.stat_service.stats.dto.ViewStats;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface StatsService {
    void addHit(EndpointHit endpointHit);

    List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique)
            throws UnsupportedEncodingException;
}
