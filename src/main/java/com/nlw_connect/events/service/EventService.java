package com.nlw_connect.events.service;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class EventService {


    private final EventRepo repository;

    @Autowired
    public EventService(EventRepo eventRepo) {
        this.repository = eventRepo;
    }

    public Events addNewEvent(Events event) throws Exception {
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));

        if (event.getStartDate().isBefore(LocalDate.now()) || event.getEndDate().isBefore(event.getStartDate())) {
            throw new Exception("event cannot be created with invalid date");
        }

        return repository.save(event);
    }

    public List<Events> getAllEvents() {
        return (List<Events>) repository.findAll();
    }

    public Events getByEventId(String eventId) {

        return repository.findByEventId((eventId));
    }

}
