package com.cjrequena.sample.component.event;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.component.projection.ProjectionHandler;
import com.cjrequena.sample.domain.aggregate.AccountAggregate;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.mapper.EventMapper;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Transactional
@Log4j2
public class AccountEventHandler extends AsyncEventHandler {

  private final List<ProjectionHandler> projectionHandlers;

  @Autowired
  public AccountEventHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties,
    List<ProjectionHandler> projectionHandlers
  ) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
    this.projectionHandlers = projectionHandlers;
  }

  @Override
  public void handle(List<EventEntity> eventEntityList) {
    final List<Event> events = this.eventMapper.mapToEventList(eventEntityList);

    Set<UUID> eventsIds = new HashSet<>();

    for (Event event : events) {
      if (log.isInfoEnabled()) {
        log.info("Handling event {} for aggregate {} with ID '{}' and aggregate version {}", event.getEventType(), getAggregateType(), event.getAggregateId(),
          event.getAggregateVersion());
      }      // Here is to set the business logic to send the incoming event through an integration channel, e.g. Kafka, SNS, SQS, AWS Lambda, Webhook, etc.

      // Here we store in a set the aggregateId of incoming events.
      eventsIds.add(event.getAggregateId());
    }

    for (UUID eventId : eventsIds) {
      final AccountAggregate aggregate = (AccountAggregate) this.retrieveOrInstantiateAggregate(eventId);
      log.info("Preparing to save or update in the projection database the aggregate {}", aggregate);

      // Save or Update the projection database
      projectionHandlers.stream()
        .filter(handler -> handler.getAggregateType().getType().equals(aggregate.getAggregateType()))
        .forEach(handler -> handler.handle(aggregate));

    }

  }

  @Nonnull
  @Override
  public AggregateType getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE;
  }

}
