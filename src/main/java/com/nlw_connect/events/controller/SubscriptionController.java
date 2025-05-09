package com.nlw_connect.events.controller;

import com.nlw_connect.events.dto.SubscriptionRankingItem;
import com.nlw_connect.events.exception.EventNotFoundException;
import com.nlw_connect.events.dto.ErrorMessage;
import com.nlw_connect.events.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Subscription", description = "Subscription for events")
public class SubscriptionController {
    @Autowired
    private SubscriptionService service;

    @Operation(
            summary = "Return ranking list by indicators number",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "eventId", description = "Event ID")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubscriptionRankingItem.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Event ranking {eventId} does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
                    ),
            }
    )
    @GetMapping("/subscription/ranking")
    public ResponseEntity<?> generateRankingByEvent(@RequestParam String eventId) {
        try {
            return ResponseEntity.status(200).body(service.getCompleteRanking(eventId));
        }
        catch(EventNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
        }

    }

}
