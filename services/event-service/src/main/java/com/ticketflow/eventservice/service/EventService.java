package com.ticketflow.eventservice.service;

import com.ticketflow.eventservice.domain.Event;
import com.ticketflow.eventservice.dto.EventRequest;
import com.ticketflow.eventservice.repo.EventRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final EventRepository repo;

    public EventService(EventRepository r) {
        repo = r;
    }

    public List<Event> all() {
        return repo.findAll();
    }

    public Event get(Long id) {
        return repo
            .findById(id)
            .orElseThrow(() ->
                new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Événement introuvable"
                )
            );
    }

    public Event create(EventRequest r) {
        if (!r.endsAt().isAfter(r.startsAt())) throw new IllegalArgumentException(
            "La date de fin doit suivre la date de début"
        );
        return repo.save(
            new Event(r.title(), r.description(), r.venue(), r.startsAt(), r.endsAt(), r.capacity(), r.price())
        );
    }

    public Event update(Long id, EventRequest r) {
        Event e = get(id);
        e.update(r.title(), r.description(), r.venue(), r.startsAt(), r.endsAt(), r.capacity(), r.price());
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.delete(get(id));
    }

    @Transactional
    public void reserve(Long id, int q) {
        if (q < 1 || repo.reserve(id, q) != 1) throw new IllegalStateException(
            "Places indisponibles ou événement non publié"
        );
    }

    @Transactional
    public void release(Long id, int q) {
        repo.release(id, q);
    }
}
