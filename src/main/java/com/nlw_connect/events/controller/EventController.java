package com.nlw_connect.events.controller;

import com.nlw_connect.events.dto.ErrorMessage;
import com.nlw_connect.events.dto.SubscriptionResponse;
import com.nlw_connect.events.dto.UserIndicatorNotFoundException;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.exception.SubscriptionConflictException;
import com.nlw_connect.events.model.Events;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.service.EmailService;
import com.nlw_connect.events.service.EventService;
import com.nlw_connect.events.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

//  Proteger rota na criação de eventos
    @Operation( summary = "create event" )
    @PostMapping()
    public Events addEvent(@RequestBody Events newEvent) {
        return eventService.addNewEvent(newEvent);
    }


    @Operation( summary = "List all event")
    @GetMapping()
    public List<Events> getAllEvents() {
        return eventService.getAllEvents();
    }


    @Operation( summary = "Get event by ID" )
    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventByEventId(@PathVariable("eventId") String eventId) {
        Events evt = eventService.getById(eventId);
        if (evt != null) {

            return ResponseEntity.ok().body(evt);
        }

        return ResponseEntity.notFound().build();
    }


    @Operation(
            summary = "subscribe to a event",
            parameters = {
                    @Parameter(in = ParameterIn.DEFAULT, name = "subscriber", description = "new subscriber object"),
                    @Parameter(in = ParameterIn.QUERY, name = "eventId", description = "Event ID"),
                    @Parameter(in = ParameterIn.QUERY, name = "userId", description = "User ID")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "subscription was created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @PostMapping("/{eventId}/subscription")
    public ResponseEntity<?> subscribe(@RequestBody User subscriber, //novo usuário
                                       @PathVariable("eventId") String eventId,
                                       @Param("userId") String userId) { //usuário que indicou

        try {
            SubscriptionResponse subResp = subService.createSub(eventId, subscriber, userId);

            if (subResp != null) {

                emailService.sendConfirmationSubscription(subscriber, eventId);
                System.out.println(subscriber.getEmail());
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


    @Operation(
            summary = "generate event certificate",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "eventId", description = "Event ID"),
                    @Parameter(in = ParameterIn.QUERY, name = "userId", description = "User ID")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "certificate was generated",
                            content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
                    ),
                    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @GetMapping("certificate")
    public ResponseEntity<byte[]> certificate(@RequestParam("userId") String userId, @RequestParam("eventId") String eventId) throws IOException, MessagingException {
        ByteArrayOutputStream pdf = emailService.sendEventCertificate(userId, eventId);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .body(pdf.toByteArray());
    }
}
