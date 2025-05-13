package com.nlw_connect.events.model;


import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.domain.entities.User;

import java.util.HashMap;
import java.util.Map;
//


public record EmailResponse(User subscriber, Events event) {
    public Map<String, Object> userData() {
        Map<String, Object> data = new HashMap<>();
        data.put("nome", subscriber.getName());
        data.put("evento", event.getTitle());
        data.put("data", event.getStartDate());
        data.put("hora", event.getStartTime());
        data.put("local", event.getLocation());
        data.put("url", "https://thisevent.com/evento/" + event.getPrettyName());
        return data;
    }
}
