package com.nlw_connect.events.service;

import com.nlw_connect.events.dto.SubscriptionRankingItem;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.exception.SubscriptionConflictException;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.exception.UserIndicatorNotFoundException;
import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.model.Role;
import com.nlw_connect.events.domain.entities.Subscription;
import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.SubscriptionRepo;
import com.nlw_connect.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SubscriptionService {

    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final SubscriptionRepo subRepo;

    @Autowired
    public SubscriptionService(EventRepo eventRepo, UserRepo userRepo, SubscriptionRepo subscriptionRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.subRepo = subscriptionRepo;
    }

    public SubscriptionResponse createSub(String eventId, User user, String userId) {
        Events event = eventRepo.findByEventId(eventId);

        if (event == null) {
            throw new EventNotFoundException("Event not found");
        }

        User foundUser = userRepo.findByEmail(user.getEmail());

        if (foundUser == null) {
            user.setCreatedAt(Instant.now());
            user.setRole(Role.USER);
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);
            foundUser = userRepo.save(user);
        }

        User indicator = null;
        if (userId != null) {
            indicator = userRepo.findById(userId).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFoundException("User indicator " + userId + " not found");
            }
        }


        Subscription subs = new Subscription();
        subs.setEvent(event);
        subs.setSubscriber(foundUser);
        subs.setIndication(indicator);

        Subscription temp = subRepo.findByEventAndSubscriber(event, foundUser);

        if (temp != null){
            throw new SubscriptionConflictException("User " + foundUser.getName() + " already exists in this event");
        }


        Subscription sub = subRepo.save(subs);
        return new SubscriptionResponse(sub.getId(), "https://codecraft.com/event/" + sub.getEvent().getPrettyName() + "/" + sub.getSubscriber().getId());
    }


    public List<SubscriptionRankingItem> getCompleteRanking(String eventId) {
        Events event = eventRepo.findByEventId(eventId);
        if (event == null) {
            throw new EventNotFoundException("Event Ranking " + eventId + " does not exist.");
        }

        return subRepo.generateRanking(event.getEventId());
    }
}
