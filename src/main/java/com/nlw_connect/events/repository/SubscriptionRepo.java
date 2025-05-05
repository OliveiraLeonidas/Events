package com.nlw_connect.events.repository;

import com.nlw_connect.events.dto.SubscriptionRankingItem;
import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.model.Subscription;
import com.nlw_connect.events.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);

    public Subscription findSubscriptionById(Integer id);

//    Lembrar de sempre setar o tipo de Dialect Database para o projeto
    @Query(value = " SELECT COUNT(subscription_number) as total_indications, indication_user_id, user_name " +
            " FROM tbl_subscription INNER JOIN tbl_user " +
            " ON tbl_subscription.indication_user_id = tbl_user.user_id " +
            " WHERE indication_user_id IS NOT NULL " +
            " AND event_id = :eventId " +
            " GROUP BY indication_user_id " +
            " ORDER BY total_indications DESC LIMIT 3; ", nativeQuery = true)
    public List<SubscriptionRankingItem> generateRanking(@Param(value = "eventId") Integer eventId );
}