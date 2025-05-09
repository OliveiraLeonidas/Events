package com.nlw_connect.events.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_number")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Events event;

    @ManyToOne
    @JoinColumn(name = "subscribed_user_id")
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "indication_user_id")
    private User indication;


}
