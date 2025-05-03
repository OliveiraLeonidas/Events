package com.nlw_connect.events.repository;

import com.nlw_connect.events.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Event, Integer> {
    public Event findByPrettyName(String prettyName);

    public Event findByEventId(int eventId);
}
