package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.EventSubscriptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventSubscriptionRepository extends CrudRepository<EventSubscriptionEntity, UUID> {

}
