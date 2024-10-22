package com.cjrequena.sample.service;

import com.cjrequena.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.sample.configuration.EventStoreConfigurationProperties.SnapshotProperties;
import com.cjrequena.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.event.Event;
import com.cjrequena.sample.entity.AggregateSnapshotEntity;
import com.cjrequena.sample.entity.EventEntity;
import com.cjrequena.sample.exception.OptimisticConcurrencyServiceException;
import com.cjrequena.sample.repository.AggregateRepository;
import com.cjrequena.sample.repository.AggregateSnapshotRepository;
import com.cjrequena.sample.repository.EventRepository;
import com.cjrequena.sample.repository.EventSubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private final ObjectMapper objectMapper;
  private final EventStoreConfigurationProperties eventStoreConfigurationProperties;

  @SneakyThrows
  public void saveAggregate(Aggregate aggregate) throws OptimisticConcurrencyServiceException {
    String aggregateType = aggregate.getAggregateType();
    UUID aggregateId = aggregate.getAggregateId();

    this.aggregateRepository.createAggregateIfAbsent(aggregateId, aggregateType);

    long expectedAggregateVersion = aggregate.getReproducedAggregateVersion();
    long newAggregateVersion = aggregate.getAggregateVersion();

    Optional<Integer> isVersionUpdated = aggregateRepository.validateAndUpdateAggregateVersionIfMatch(aggregateId, expectedAggregateVersion, newAggregateVersion);
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

    //Append new events
    List<Event> unconfirmedEventsPool = aggregate.getUnconfirmedEventsPool();
    for (Event event : unconfirmedEventsPool) {
      log.info("Appending {} event: {}", aggregateType, event);
      EventEntity eventEntity = event.mapToEventEntity();
      eventRepository.save(eventEntity);
    }

    // Create snapshot
    SnapshotProperties snapshotProperties = eventStoreConfigurationProperties.getSnapshot(aggregateType);
    boolean shouldCreateSnapshot = aggregate.getAggregateVersion() % snapshotProperties.interval() == 0 || unconfirmedEventsPool.size() >= snapshotProperties.interval();
    if (shouldCreateSnapshot) {
      log.info("Creating {} aggregate {} version {} snapshot", aggregate.getAggregateType(), aggregate.getAggregateId(), aggregate.getAggregateVersion());
      AggregateSnapshotEntity aggregateSnapshotEntity = AggregateSnapshotEntity.builder()
        .aggregateId(aggregate.getAggregateId())
        .data(this.objectMapper.writeValueAsString(aggregate))
        .aggregateVersion(aggregate.getAggregateVersion())
        .build();
      this.aggregateSnapshotRepository.save(aggregateSnapshotEntity);

    }
  }

}
