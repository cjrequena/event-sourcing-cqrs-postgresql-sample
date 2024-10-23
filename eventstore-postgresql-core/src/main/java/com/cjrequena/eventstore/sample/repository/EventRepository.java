package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.AbstractEventEntity;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface EventRepository extends CrudRepository<AbstractEventEntity, UUID> {


  /**
   * Retrieves a list of {@link EventEntity} instances associated with a specific aggregate ID.
   * This query selects all events from the `es_event` table that match the given {@code aggregateId},
   * and optionally filters the results by an aggregate version range, ordering the results in ascending
   * order by the `aggregate_version`.
   * <p>
   * The query behaves as follows:
   * <ul>
   *     <li>If {@code fromAggregateVersion} is not null, only events with an {@code aggregate_version} greater
   *         than {@code fromAggregateVersion} will be returned.</li>
   *     <li>If {@code toAggregateVersion} is not null, only events with an {@code aggregate_version} less than
   *         or equal to {@code toAggregateVersion} will be returned.</li>
   *     <li>If both {@code fromAggregateVersion} and {@code toAggregateVersion} are null, all events matching
   *         the {@code aggregateId} will be returned.</li>
   * </ul>
   *
   * @param aggregateId the unique identifier of the aggregate whose events are being retrieved. Cannot be null.
   * @param fromAggregateVersion the lower bound of the aggregate version for filtering events. If null, no lower bound is applied.
   * @param toAggregateVersion the upper bound of the aggregate version for filtering events. If null, no upper bound is applied.
   *
   * @return a list of {@link EventEntity} instances that match the given criteria. The list will be ordered
   *         by the {@code aggregate_version} in ascending order. Returns an empty list if no matching events are found.
   *
   * @throws IllegalArgumentException if the {@code aggregateId} is null.
   */
  @Query(value = """
           SELECT *
             FROM es_event
            WHERE aggregate_id = :aggregateId
              AND (:fromAggregateVersion IS NULL OR aggregate_version > :fromAggregateVersion)
              AND (:toAggregateVersion IS NULL OR aggregate_version <= :toAggregateVersion)
            ORDER BY aggregate_version ASC
           """, nativeQuery = true)
  List<EventEntity> retrieveEventsByAggregateId(
    @Param("aggregateId") UUID aggregateId,
    @Param("fromAggregateVersion") Long fromAggregateVersion,
    @Param("toAggregateVersion") Long toAggregateVersion
  );
}
