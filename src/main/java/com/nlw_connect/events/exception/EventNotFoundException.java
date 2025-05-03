package com.nlw_connect.events.exception;

import org.springframework.web.bind.annotation.PathVariable;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
