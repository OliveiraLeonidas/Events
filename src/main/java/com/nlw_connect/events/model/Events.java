package com.nlw_connect.events.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "pretty_name", nullable = false, unique = true)
    private String prettyName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "price", nullable = false) //price in cents
    private Integer price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;



}
