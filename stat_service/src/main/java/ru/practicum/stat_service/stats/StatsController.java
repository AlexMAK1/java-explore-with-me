package ru.practicum.stat_service.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat_service.stats.dto.EndpointHit;
import ru.practicum.stat_service.stats.dto.ViewStats;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RestController
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public void createHit(@Valid @RequestBody EndpointHit endpointHit) {
        statsService.addHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStatistics(@RequestParam(name = "start") @NotNull String start,
                                         @RequestParam(name = "end") @NotNull String end,
                                         @RequestParam(name = "uris") List<String> uris,
                                         @RequestParam(name = "unique", defaultValue = "false") Boolean unique) throws UnsupportedEncodingException {
        return statsService.getStatistics(start, end, uris, unique);
    }
}
