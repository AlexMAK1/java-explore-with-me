package ru.practicum.main_service.compilations.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.main_service.events.model.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "compilations")
@NoArgsConstructor
public class Compilation {

    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "event_compilations",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "compilation_id")}
    )
    @ToString.Exclude
    private Set<Event> events = new HashSet<>();

    public Compilation(String title, Boolean pinned, Set<Event> events) {
        this.title = title;
        this.pinned = pinned;
        this.events = events;
    }
}
