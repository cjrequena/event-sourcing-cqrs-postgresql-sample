package com.cjrequena.sample.service.command;

import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.command.Command;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.exception.service.EventStoreOptimisticConcurrencyServiceException;
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
public abstract class CommandHandler<T extends Command> {

  protected final EventStoreService eventStoreService;
  protected final AggregateFactory aggregateFactory;
  protected final EventMapper eventMapper;
  protected final EventStoreConfigurationProperties eventStoreConfigurationProperties;

  public abstract Aggregate handle(@Nonnull Command command) throws EventStoreOptimisticConcurrencyServiceException;

  @Nonnull
  public abstract Class<T> getCommandType();

  @Nonnull
  public abstract AggregateType getAggregateType();

  protected Aggregate retrieveOrInstantiateAggregate(UUID aggregateId) {
    final EventStoreConfigurationProperties.SnapshotProperties snapshotConfiguration = eventStoreConfigurationProperties.getSnapshot(
      getAggregateType().getType());
    if (snapshotConfiguration.enabled()) {
      return retrieveAggregateFromSnapshot(aggregateId)
        .orElseGet(() -> createAndReproduceAggregate(aggregateId));
    } else {
      return createAndReproduceAggregate(aggregateId);
    }
  }

  protected Optional<Aggregate> retrieveAggregateFromSnapshot(UUID aggregateId) {
    Optional<Aggregate> optionalAggregate = eventStoreService.retrieveAggregateSnapshot(getAggregateType().getClazz(), aggregateId, null);
    return optionalAggregate.map(aggregate -> {
      List<Event> events = retrieveEvents(aggregateId, aggregate.getAggregateVersion());
      aggregate.reproduceFromEvents(events);
      return aggregate;
    });
  }

  protected Aggregate createAndReproduceAggregate(UUID aggregateId) {
    log.info("Snapshot not found for Aggregate ID: {}. Reconstituting from events.", aggregateId);
    Aggregate aggregate = aggregateFactory.newInstance(getAggregateType().getClazz(), aggregateId);
    List<Event> events = retrieveEvents(aggregateId, null);
    aggregate.reproduceFromEvents(events);
    return aggregate;
  }

  protected List<Event> retrieveEvents(UUID aggregateId, Long fromVersion) {
    return eventMapper.mapToEventList(eventStoreService.retrieveEventsByAggregateId(aggregateId, fromVersion, null));
  }
}
