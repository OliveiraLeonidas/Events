package com.nlw_connect.events.service;

import com.nlw_connect.events.dto.SubscriptionRankingItem;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.exception.SubscriptionConflictException;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.dto.UserIndicatorNotFoundException;
import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.SubscriptionRepo;
import com.nlw_connect.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SubscriptionRepo subRepo;

    public SubscriptionResponse createSubscription(String eventName, User user, Integer userId) {

        Event event = eventRepo.findByPrettyName(eventName);

        if (event == null) {
            throw new EventNotFoundException("Event " + eventName + " not found");
        }

        User foundUser = userRepo.findByEmail((user.getEmail()));
        if (foundUser == null) {
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

    };

    public SubscriptionResponse createSub(Integer eventId, User user, Integer userId) {
        Event event = eventRepo.findByEventId(eventId);

        if (event == null) {
            throw new EventNotFoundException("Event not found");
        }

        User foundUser = userRepo.findUserById(user.getId());
        if (foundUser == null) { // caso 1: se o usuário não existir, então deve ser criado
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


    public List<SubscriptionRankingItem> getCompleteRanking(Integer eventId) {
        Event event = eventRepo.findByEventId(eventId);
        if (event == null) {
            throw new EventNotFoundException("Event Ranking " + eventId + " does not exist.");
        }

        return subRepo.generateRanking(event.getEventId());
    }
}
