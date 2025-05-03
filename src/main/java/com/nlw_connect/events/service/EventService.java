package com.nlw_connect.events.service;

import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Regras de negócios
@Service
public class EventService {

    @Autowired
    private EventRepo repository;

    public Event addNewEvent(Event event) {
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>)repository.findAll();
    }

    public Event getByPrettyName(String prettyName) {
        return repository.findByPrettyName(prettyName);
    }

    public Event getById(Integer eventId) {

        return repository.findByEventId((eventId));
    }

}
