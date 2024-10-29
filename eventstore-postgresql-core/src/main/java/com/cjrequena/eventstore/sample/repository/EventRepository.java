package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.AbstractEventEntity;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
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

  @Query(value = """
    SELECT 
        event.id, 
        event.offset_id, 
        event.offset_txid, 
        event.aggregate_id, 
        event.aggregate_version, 
        event.event_type, 
        event.data_content_type, 
        event.data, 
        event.data_base64, 
        event.offset_date_time, 
        event.extension
    FROM ES_EVENT event
    JOIN ES_AGGREGATE aggregate on aggregate.id = event.aggregate_id
    WHERE aggregate.aggregate_type = :aggregateType
      AND (event.offset_txid, event.offset_id) > (:offsetTxId ::text::xid8, :offsetId)
      AND event.offset_txid < pg_snapshot_xmin(pg_current_snapshot())
    ORDER BY event.offset_txid ASC, event.offset_id ASC
    """, nativeQuery = true)
  List<EventEntity> retrieveEventsByAggregateTypeAfterOffsetTxIAndOffsetId(
    @Param("aggregateType") @NonNull String aggregateType,
    @Param("offsetTxId") @NonNull Long offsetTxId,
    @Param("offsetId") @NonNull Long offsetId);
}
