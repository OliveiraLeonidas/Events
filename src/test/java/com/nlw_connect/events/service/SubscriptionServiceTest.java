package com.nlw_connect.events.service;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.domain.entities.Subscription;
import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.dto.UserDTO;
import com.nlw_connect.events.model.Role;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.SubscriptionRepo;
import com.nlw_connect.events.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SubscriptionServiceTest {

    @Mock
    private EventRepo eventRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private SubscriptionRepo subRepo;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should create an subscription")
    void createSub() {
        // setup
        String eventId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Events newEvent = new Events(
                eventId,
                "summit 2024",
                "summit-2024",
                "online",
                0,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                LocalTime.now(),
                LocalTime.now());
        String email = "leonidas@gmail.com";
        User user = new User(userId, "Leonidas", email, "olivleo", "951357", Role.USER, Instant.now());

        when(eventRepo.findByEventId(eventId)).thenReturn(newEvent);
        when(userRepo.findByEmail(email)).thenReturn(user);
        when(subRepo.findByEventAndSubscriber(newEvent, user)).thenReturn(null);
        when(subRepo.save(any(Subscription.class))).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            s.setId(1);
            return s;
        });

        SubscriptionResponse response = subscriptionService.createSub(eventId, user, null);

        assertNotNull(response);
        assertNotNull(response.subscriptionNumber());

        verify(subRepo, times(1)).save(any(Subscription.class));

    }

    @Test
    void getCompleteRanking() {
    }
}