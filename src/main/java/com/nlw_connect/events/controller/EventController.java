package com.nlw_connect.events.controller;

import com.nlw_connect.events.dto.ErrorMessage;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.dto.UserIndicatorNotFoundException;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.exception.SubscriptionConflictException;
import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.repository.EventRepo;
import com.nlw_connect.events.repository.SubscriptionRepo;
import com.nlw_connect.events.repository.UserRepo;
import com.nlw_connect.events.service.EmailService;
import com.nlw_connect.events.service.EventService;
import com.nlw_connect.events.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@Tag(name = "Event", description = "CRUD for Events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private SubscriptionService subService;

    @Autowired
    private EmailService emailService;
//
//    @Autowired
//    private EventRepo eventRepo;
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private SubscriptionRepo subscriptionRepo;

    @PostMapping()
    @Operation(
            summary = "create event"
    )
    public Event addEvent(@RequestBody Event newEvent) {
        return eventService.addNewEvent(newEvent);
    }


    @Operation(
            summary = "List all event"
    )
    @GetMapping()
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Operation(
            summary = "Get event by Pretty Name"
    )

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventByEventId(@PathVariable("eventId") int eventId) {
        Event evt = eventService.getById(eventId);
        if (evt != null) {
            return ResponseEntity.ok().body(evt);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiResponse(responseCode = "200", description = "subscription was created",
            content = @Content(schema = @Schema(implementation = Subscription.class))
    )
    @Operation(
            summary = "subscribe to a event"
    )
    @PostMapping("/{eventId}/subscription")
    public ResponseEntity<?> subscribe(@RequestBody User subscriber, //novo usuário
                                       @PathVariable("eventId") Integer eventId,
                                       @Param("userId") Integer userId) { //usuário que indicou

        try {
            SubscriptionResponse subResp = subService.createSub(eventId, subscriber, userId);

            if (subResp != null) {

                emailService.sendConfirmationSubscription(subscriber, eventId);
                System.out.println(subscriber.getName());
                System.out.println(eventId);
                return ResponseEntity.ok(subResp);
            }
        }
        catch (EventNotFoundException | UserIndicatorNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }
        catch (SubscriptionConflictException e) {
            return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
        }

        return ResponseEntity.badRequest().build();
    }
}
