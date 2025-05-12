package com.nlw_connect.events.service;

import com.nlw_connect.events.dto.SubscriptionRankingItem;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.exception.SubscriptionConflictException;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.dto.UserIndicatorNotFoundException;
import com.nlw_connect.events.model.Events;
import com.nlw_connect.events.model.Role;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
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

    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SubscriptionRepo subRepo;

    public SubscriptionResponse createSub(String eventId, User user, String userId) {
        Events event = eventRepo.findByEventId(eventId);
        System.out.println("Encontrou o evento: " + event);
        if (event == null) {
            throw new EventNotFoundException("Event not found");
        }

        User foundUser = userRepo.findByEmail(user.getEmail());
        System.out.println("Usuário encontrado: " + foundUser);
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


        // se o evento, user e indicador existem, então uma inscrição pode ser criada
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
