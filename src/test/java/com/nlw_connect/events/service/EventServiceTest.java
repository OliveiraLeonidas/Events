package com.nlw_connect.events.service;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.repository.EventRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EventServiceTest {
    @Mock
    private EventRepo repository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should create event successfully when everything is OK")
    void addNewEventSuccess() throws Exception {
        String id = UUID.randomUUID().toString();
        Events newEvent = new Events(id, "summit 2024", "summit-2024", "online", 0, LocalDate.now().plusDays(1L), LocalDate.now().plusDays(3L), LocalTime.now(), LocalTime.now());

        when(repository.save(any(Events.class))).thenReturn(newEvent);

        eventService.addNewEvent(newEvent);
        verify(repository, times(1)).save(newEvent);
    }

    @Test
    @DisplayName("Should thrown an error when trying create an event with start or end date before current date")
    void addNewEventFailed() throws Exception {
        String id = UUID.randomUUID().toString();
        Events newEvent = new Events(id, "summit 2024", "summit-2024", "online", 0, LocalDate.now(), LocalDate.now().minusDays(1), LocalTime.now(), LocalTime.now());

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            eventService.addNewEvent(newEvent);
        });

        Assertions.assertEquals("event cannot be created with invalid date", thrown.getMessage());
        verify(repository, never()).save(any());
    }

}