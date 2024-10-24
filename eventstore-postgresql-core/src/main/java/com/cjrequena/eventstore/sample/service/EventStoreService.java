package com.cjrequena.eventstore.sample.service;

import com.cjrequena.eventstore.sample.common.util.JsonUtil;
import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.eventstore.sample.configuration.EventStoreConfigurationProperties.SnapshotProperties;
import com.cjrequena.eventstore.sample.domain.aggregate.Aggregate;
import com.cjrequena.eventstore.sample.domain.event.Event;
import com.cjrequena.eventstore.sample.entity.AbstractEventEntity;
import com.cjrequena.eventstore.sample.entity.AggregateSnapshotEntity;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import com.cjrequena.eventstore.sample.exception.service.OptimisticConcurrencyServiceException;
import com.cjrequena.eventstore.sample.repository.AggregateRepository;
import com.cjrequena.eventstore.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.eventstore.sample.repository.EventRepository;
import com.cjrequena.eventstore.sample.repository.EventSubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class EventStoreService {

  private final AggregateRepository aggregateRepository;
  private final AggregateSnapshotRepository aggregateSnapshotRepository;
  private final EventRepository eventRepository;
  private final EventSubscriptionRepository eventSubscriptionRepository;
  private final EventStoreConfigurationProperties eventStoreConfigurationProperties;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  public void saveAggregate(Aggregate aggregate) throws OptimisticConcurrencyServiceException {
    String aggregateType = aggregate.getAggregateType();
    UUID aggregateId = aggregate.getAggregateId();

    // Create new aggregate if does not exist.
    this.aggregateRepository.createAggregateIfAbsent(aggregateId, aggregateType);

    long expectedAggregateVersion = aggregate.getReproducedAggregateVersion();
    long newAggregateVersion = aggregate.getAggregateVersion();

    Optional<Integer> isVersionUpdated = aggregateRepository.verifyAndUpdateAggregateVersionIfMatch(aggregateId, expectedAggregateVersion, newAggregateVersion);
    if (isVersionUpdated.isEmpty()) {
      String errorMessage = String.format(
        "Optimistic concurrency control error in aggregate: %s id: %s. Actual version doesn't match expected version: %s",
        aggregateType,
        aggregateId,
        expectedAggregateVersion
      );
      log.warn(errorMessage);
      throw new OptimisticConcurrencyServiceException(errorMessage);
    }

    // Append new events
    List<Event> unconfirmedEventsPool = aggregate.getUnconfirmedEventsPool();
    for (Event event : unconfirmedEventsPool) {
      log.info("Appending {} event: {}", aggregateType, event);
      AbstractEventEntity eventEntity = event.mapToEventEntity();
      eventRepository.save(eventEntity);
    }

    // Create snapshot
    SnapshotProperties snapshotProperties = eventStoreConfigurationProperties.getSnapshot(aggregateType);
    boolean shouldCreateSnapshot = aggregate.getAggregateVersion() % snapshotProperties.interval() == 0 || unconfirmedEventsPool.size() >= snapshotProperties.interval();
    if (shouldCreateSnapshot) {
      log.info("Creating {} aggregate {} version {} snapshot", aggregate.getAggregateType(), aggregate.getAggregateId(), aggregate.getAggregateVersion());
      AggregateSnapshotEntity aggregateSnapshotEntity = AggregateSnapshotEntity.builder()
        .aggregateId(aggregate.getAggregateId())
        .aggregateVersion(aggregate.getAggregateVersion())
        .aggregateType(aggregate.getAggregateType())
        .data(this.objectMapper.writeValueAsString(aggregate))
        .build();
      this.aggregateSnapshotRepository.save(aggregateSnapshotEntity);
    }
  }

  public Optional<Aggregate> retrieveAggregateSnapshot(Class<? extends Aggregate> aggregateClass, UUID aggregateId, @Nullable Long aggregateVersion) {
    final AggregateSnapshotEntity aggregateSnapshotEntity = this.aggregateSnapshotRepository.retrieveAggregateSnapshot(aggregateId, null);
    final Aggregate aggregate = fromSnapshotToAggregate(aggregateSnapshotEntity, aggregateClass);
    return Optional.ofNullable(aggregate);
  }

  @Transactional(readOnly = true)
  public List<EventEntity> retrieveEventsByAggregateId(UUID aggregateId, @Nullable Long fromAggregateVersion, @Nullable Long toAggregateVersion) {
    // Validate input
    if (aggregateId == null) {
      throw new IllegalArgumentException("aggregateId cannot be null");
    }
    // Query the repository, with optional parameters for version range
    return eventRepository.retrieveEventsByAggregateId(aggregateId, fromAggregateVersion, toAggregateVersion);
  }

  @SneakyThrows
  private Aggregate fromSnapshotToAggregate(AggregateSnapshotEntity aggregateSnapshotEntity, Class<? extends Aggregate> aggregateClass) {
    String json = aggregateSnapshotEntity.getData();
    return JsonUtil.jsonStringToObject(json, aggregateClass);
  }

}
