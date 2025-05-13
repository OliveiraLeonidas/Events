package com.nlw_connect.events.repository;

import com.nlw_connect.events.domain.entities.Events;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Events, String> {
    Events findByPrettyName(String prettyName);
    Events findByEventId(String eventId);
}
