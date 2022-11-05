package ru.practicum.main_service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.events.model.Event;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEvent(Event event);

    List<Request> findAllByRequester(User user);
}
