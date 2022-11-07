package ru.practicum.stat_service.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat_service.stats.model.Hit;

import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select count (h.id) from Hit as h where h.timestamp>=?1 and h.timestamp<=?2 and h.uri like ?3")
    Long getStatistics(LocalDateTime startTime, LocalDateTime endTime, String uri);

    @Query("select count (h.uri) from Hit as h where h.timestamp>=?1 and h.timestamp<=?2 and h.uri like ?3 order by h.ip")
    Long getUniqueStatistics(LocalDateTime startTime, LocalDateTime endTime, String uri);
}
