package com.cjrequena.sample.service;

import com.cjrequena.sample.configuration.EventStoreConfigurationProperties;
import com.cjrequena.sample.configuration.EventStoreConfigurationProperties.SnapshotProperties;
import com.cjrequena.sample.domain.aggregate.Aggregate;
import com.cjrequena.sample.domain.event.Event;
import com.cjrequena.sample.entity.AbstractEventEntity;
import com.cjrequena.sample.entity.AggregateSnapshotEntity;
import com.cjrequena.sample.entity.EventEntity;
import com.cjrequena.sample.exception.service.OptimisticConcurrencyServiceException;
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
        .data(this.objectMapper.writeValueAsString(aggregate))
        .aggregateVersion(aggregate.getAggregateVersion())
        .build();
      this.aggregateSnapshotRepository.save(aggregateSnapshotEntity);
    }
  }


  /**
   * Retrieves a list of {@link EventEntity} instances associated with the specified aggregate ID,
   * optionally filtered by a range of aggregate versions.
   * <p>
   * This method is typically used to retrieve events for a specific aggregate, allowing for
   * filtering based on the provided version range. If both {@code fromAggregateVersion} and
   * {@code toAggregateVersion} are {@code null}, all events for the given aggregate ID are returned.
   * <p>
   * This operation is executed within a read-only transaction, ensuring optimal performance
   * for data retrieval without any modification to the underlying data.
   * <p>
   * <strong>Usage Example:</strong>
   * <pre>
   * {@code
   * UUID aggregateId = UUID.randomUUID();
   * Long fromVersion = 1L;
   * Long toVersion = 10L;
   * List<EventEntity> events = eventService.retrieveEventsByAggregateId(aggregateId, fromVersion, toVersion);
   * if (events.isEmpty()) {
   *     // Handle case when no events are found
   * }
   * }
   * </pre>
   *
   * @param aggregateId the unique identifier of the aggregate whose events are being retrieved.
   *                    This parameter cannot be {@code null}.
   * @param fromAggregateVersion the lower bound (inclusive) for filtering events by aggregate version.
   *                             If {@code null}, no lower bound filter is applied.
   * @param toAggregateVersion the upper bound (inclusive) for filtering events by aggregate version.
   *                           If {@code null}, no upper bound filter is applied.
   *
   * @return a list of {@link EventEntity} instances that match the specified criteria,
   *         ordered by {@code aggregate_version} in ascending order. If no events match the
   *         criteria, an empty list is returned.
   *
   * @throws IllegalArgumentException if {@code aggregateId} is {@code null}.
   *
   * @see EventEntity
   * @see EventRepository#retrieveEventsByAggregateId(UUID, Long, Long)
   */
  @Transactional(readOnly = true)
  public List<EventEntity> retrieveEventsByAggregateId(UUID aggregateId, Long fromAggregateVersion, Long toAggregateVersion) {

    // Validate input
    if (aggregateId == null) {
      throw new IllegalArgumentException("aggregateId cannot be null");
    }

    // Query the repository, with optional parameters for version range
    return eventRepository.retrieveEventsByAggregateId(aggregateId, fromAggregateVersion, toAggregateVersion);
  }


}
