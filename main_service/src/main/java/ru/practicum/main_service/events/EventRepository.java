package ru.practicum.main_service.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAllByInitiator(User user, PageRequest pageRequest);

    @Query("select e from Event as e where e.id in :ids")
    Set<Event> findAllEvents(List<Long> ids);

    Event findByInitiator(User user);
}
