package ru.practicum.main_service.compilations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
        Page<Compilation> findAll(Pageable pageable);
}
