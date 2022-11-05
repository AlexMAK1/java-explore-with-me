package ru.practicum.stat_service.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stat_service.stats.dto.EndpointHit;
import ru.practicum.stat_service.stats.dto.ViewStats;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public void addHit(EndpointHit endpointHit) {
        log.info("Save hit : {}", StatsConverter.toHit(endpointHit));
        statsRepository.save(StatsConverter.toHit(endpointHit));
    }

    @Override
    public List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique)
            throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime;
        LocalDateTime endTime;
        String app = "main_service";
        List<ViewStats> views = new ArrayList<>();
        ViewStats viewStats = new ViewStats();
        startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8.toString()), formatter);
        endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8.toString()), formatter);
        if (unique) {
            for (String uri : uris) {
                viewStats.setUri(uri);
                viewStats.setApp(app);
                viewStats.setHits(statsRepository.getUniqueStatistics(startTime, endTime, uri));
                views.add(viewStats);
            }
        } else {
            for (String uri : uris) {
                viewStats.setUri(uri);
                viewStats.setApp(app);
                viewStats.setHits(statsRepository.getStatistics(startTime, endTime, uri));
                views.add(viewStats);
            }
        }
        log.info("Get views : {}", views);
        return views;
    }
}
