package com.nlw_connect.events.bdd;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.service.EventService;
import org.jbehave.core.annotations.*;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BehaveEventTest {
    private EventService eventService;
    private EventRepo eventRepo;
    private Events event;

    private Exception exception;

    @BeforeStories
    public void setup() {
        eventRepo = mock(EventRepo.class);
        eventService = new EventService(eventRepo);
    }

    @Given("that I have an event with the name \"$title\", prettyName \"$prettyName\", and location as \"$location\"")
    public void basicEvent(String title, String prettyName, String location) {
        String id = UUID.randomUUID().toString();
        event = new Events();
        event.setEventId(id);
        event.setTitle(title);
        event.setPrettyName(prettyName);
        event.setPrice(0);
        event.setLocation(location);
    }


    @Given("with a future start date and a future end date")
    public void setValidDates() {
        event.setStartDate( LocalDate.now().plusDays(1));
        event.setEndDate(LocalDate.now().plusDays(3));
        event.setStartTime(LocalTime.now());
        event.setEndTime(LocalTime.now().plusHours(3));
    }


    @When("I try to create this event")
    public void tryCreateEvent() {
        try {
            when(eventRepo.save(any(Events.class))).thenReturn(event);
            eventService.addNewEvent(event);
            System.out.println("Event saved: " + event.getTitle());
        } catch (Exception e) {
            exception = e;
            System.out.println(e.getMessage());
        }
    }

    @Then("the event should be created successfully")
    public void verifyCreatedEvent() {
        verify(eventRepo, times(1)).save(event);
        assertNull(exception, "Nenhuma exceção deveria ser lançada");
        System.out.println("Run without exceptions");
    }

}
