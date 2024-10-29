package com.cjrequena.sample.component;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.service.AggregateFactory;
import com.cjrequena.eventstore.sample.service.EventStoreService;
import com.cjrequena.sample.domain.aggregate.AggregateType;
import com.cjrequena.sample.mapper.EventMapper;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@Log4j2
public class AccountEventHandler extends AsyncEventHandler {

  @Autowired
  public AccountEventHandler(
    EventStoreService eventStoreService,
    AggregateFactory aggregateFactory,
    EventMapper eventMapper,
    EventStoreConfigurationProperties eventStoreConfigurationProperties) {
    super(eventStoreService, aggregateFactory, eventMapper, eventStoreConfigurationProperties);
  }

  @Override
  public void handle(List<EventEntity> eventEntityList) {
    final List<Event> events = this.eventMapper.mapToEventList(eventEntityList);
    for (Event event : events) {
      log.info("A new event {} is being handle for aggregate  with ID '{}' and aggregate version {}", event.getEventType(), event.getAggregateId(), event.getAggregateVersion());
      // Here is to set the business logic to send the incoming event through an integration channel, e.g. Kafka, SNS, SQS, AWS Lambda, Webhook, etc.
    }

    Event lastEvent = events.getLast();
    final Aggregate aggregate = this.retrieveOrInstantiateAggregate(lastEvent.getAggregateId());
    log.info("Preparing to save or update in the projection database the aggregate {}", aggregate);
    // Store the aggregate into the projection database.
  }

  @Nonnull
  @Override
  public String getAggregateType() {
    return AggregateType.ACCOUNT_AGGREGATE.getAggregateType();
  }

}
