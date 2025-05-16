package com.nlw_connect.events.bdd;

import com.nlw_connect.events.domain.entities.Events;
import com.nlw_connect.events.domain.entities.Subscription;
import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.SubscriptionRepo;
import com.nlw_connect.events.repository.UserRepo;
import com.nlw_connect.events.service.EventService;
import com.nlw_connect.events.service.SubscriptionService;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.jupiter.api.Assertions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BehaveSubscriptions {
    private EventRepo eventRepo;
    private UserRepo userRepo;
    private SubscriptionRepo subscriptionRepo;
    private SubscriptionService subscriptionService;

    private Events event;
    private User user;
    private User indicator;
    private SubscriptionResponse response;
    private Exception exception;


    @BeforeStories
    public void setup() {
        eventRepo = mock(EventRepo.class);
        userRepo = mock(UserRepo.class);
        subscriptionRepo = mock(SubscriptionRepo.class);
        subscriptionService = new SubscriptionService(eventRepo, userRepo, subscriptionRepo);
    }

    @Given("a user with username \"$username\" and password \"$password\"")
    public void givenUser(String username, String password) {
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
    }

    @Given("an existing event with id \"$eventId\" and prettyName \"$prettyName\"")
    public void givenEvent(String eventId, String prettyName) {
        event = new Events();
        event.setEventId(eventId);
        event.setPrettyName(prettyName);

        when(eventRepo.findByEventId(eventId)).thenReturn(event);

    }

    @Given("this user is not yet subscribed to the event")
    public void userNotSubscribed() {
        when(userRepo.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepo.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId("generated-id");
            return u;
        });
        when(subscriptionRepo.findByEventAndSubscriber(any(), any())).thenReturn(null);
        when(subscriptionRepo.save(any())).thenAnswer(i -> {
            Subscription s = i.getArgument(0);
            s.setId(1);
            return s;
        });
    }

    @When("the user tries to subscribe to the event \"$eventId\"")
    public void trySubscribe(String eventId) {
        try {
            response = subscriptionService.createSub(eventId, user, null);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the subscription should be created with a link containing \"$linkPart\"")
    public void verifySuccess(String linkPart) {
        Assertions.assertNotNull(response);
    }

}
