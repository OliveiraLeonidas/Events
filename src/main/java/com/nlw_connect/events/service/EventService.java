package com.nlw_connect.events.service;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Regras de negócios
@Service
public class EventService {

    @Autowired
    private EventRepo repository;

    public Events addNewEvent(Events event) {
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return repository.save(event);
    }

    public List<Events> getAllEvents() {
        return (List<Events>)repository.findAll();
    }

    public Events getByPrettyName(String prettyName) {
        return repository.findByPrettyName(prettyName);
    }

    public Events getById(String eventId) {

        return repository.findByEventId((eventId));
    }

}
