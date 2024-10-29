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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public abstract class AsyncEventHandler {

  protected final EventStoreService eventStoreService;
  protected final AggregateFactory aggregateFactory;
  protected final EventMapper eventMapper;
  protected final EventStoreConfigurationProperties eventStoreConfigurationProperties;

  //void handle(EventEntity eventEntity);

  public abstract void handle(List<EventEntity> eventEntityList);

  @Nonnull
  public abstract String getAggregateType();

  protected Aggregate retrieveOrInstantiateAggregate(UUID aggregateId) {
    final EventStoreConfigurationProperties.SnapshotProperties snapshotConfiguration = eventStoreConfigurationProperties.getSnapshot(
      AggregateType.ACCOUNT_AGGREGATE.getAggregateType());
    if (snapshotConfiguration.enabled()) {
      return retrieveAggregateFromSnapshot(aggregateId)
        .orElseGet(() -> createAndReproduceAggregate(aggregateId));
    } else {
      return createAndReproduceAggregate(aggregateId);
    }
  }

  protected Optional<Aggregate> retrieveAggregateFromSnapshot(UUID aggregateId) {
    Optional<Aggregate> optionalAggregate = eventStoreService.retrieveAggregateSnapshot(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId, null);
    return optionalAggregate.map(aggregate -> {
      List<Event> events = retrieveEvents(aggregateId, aggregate.getAggregateVersion());
      aggregate.reproduceFromEvents(events);
      return aggregate;
    });
  }

  protected Aggregate createAndReproduceAggregate(UUID aggregateId) {
    log.info("Snapshot not found for Aggregate ID: {}. Reconstituting from events.", aggregateId);
    Aggregate aggregate = aggregateFactory.newInstance(AggregateType.ACCOUNT_AGGREGATE.getAggregateClass(), aggregateId);
    List<Event> events = retrieveEvents(aggregateId, null);
    aggregate.reproduceFromEvents(events);
    return aggregate;
  }

  protected List<Event> retrieveEvents(UUID aggregateId, Long fromVersion) {
    return eventMapper.mapToEventList(eventStoreService.retrieveEventsByAggregateId(aggregateId, fromVersion, null));
  }

}
