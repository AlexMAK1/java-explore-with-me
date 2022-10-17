package ru.practicum.main_service.user;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
