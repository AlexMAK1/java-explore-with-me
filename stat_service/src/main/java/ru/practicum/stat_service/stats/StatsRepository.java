package ru.practicum.stat_service.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat_service.stats.model.Hit;

import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select COUNT(id) from Hit where timestamp>=?1 AND timestamp<=?2 AND uri LIKE ?3")
    Long getStatistics(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select COUNT(id) from Hit WHERE timestamp>=?1 AND timestamp<=?2 AND uri LIKE ?3 ORDER BY ip")
    Long getUniqueStatistics(LocalDateTime start, LocalDateTime end, String uri);

}
