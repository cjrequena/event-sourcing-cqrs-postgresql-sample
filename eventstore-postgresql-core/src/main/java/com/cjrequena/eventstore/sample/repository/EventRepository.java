package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.AbstractEventEntity;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing and managing event data stored in the event store database.
 * This interface extends {@link CrudRepository}, providing basic CRUD operations and additional
 * custom queries for retrieving and managing event-related data.
 */
@Repository
@Transactional
public interface EventRepository extends CrudRepository<AbstractEventEntity, UUID> {

  /**
   * Retrieves a list of {@link EventEntity} instances associated with a specific aggregate ID.
   * This query selects all events from the `es_event` table that match the given {@code aggregateId},
   * and optionally filters the results by an aggregate version range, ordering the results in ascending
   * order by the `aggregate_version`.
   *
   * @param aggregateId the unique identifier of the aggregate whose events are being retrieved. Cannot be null.
   * @param fromAggregateVersion the lower bound of the aggregate version for filtering events. If null, no lower bound is applied.
   * @param toAggregateVersion the upper bound of the aggregate version for filtering events. If null, no upper bound is applied.
   * @return a list of {@link EventEntity} instances that match the given criteria. The list will be ordered
   *         by the {@code aggregate_version} in ascending order. Returns an empty list if no matching events are found.
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

  /**
   * Retrieves a list of {@link EventEntity} instances associated with a specific aggregate type
   * and occurring after the specified offset transaction ID and offset ID. Results are ordered
   * by offset transaction ID and offset ID in ascending order.
   *
   * @param aggregateType the type of aggregate whose events are being retrieved. Cannot be null.
   * @param offsetTxId the transaction ID offset to start retrieving events from. Cannot be null.
   * @param offsetId the offset ID to start retrieving events from. Cannot be null.
   * @return a list of {@link EventEntity} instances matching the specified criteria.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
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
  List<EventEntity> retrieveEventsByAggregateTypeAfterOffsetTxIdAndOffsetId(
    @Param("aggregateType") @NotNull String aggregateType,
    @Param("offsetTxId") @NotNull Long offsetTxId,
    @Param("offsetId") @NotNull Long offsetId);

  /**
   * Retrieves the latest {@link EventEntity} instances for a specific aggregate type and a list of aggregate IDs.
   * This query groups events by aggregate ID and aggregate type and selects the most recent event for each group.
   *
   * @param aggregateType the type of aggregate to retrieve events for. Cannot be null.
   * @param aggregateIds a list of aggregate IDs to filter events by. Cannot be null.
   * @return a list of the latest {@link EventEntity} instances for each specified aggregate ID, ordered by aggregate ID.
   * @throws IllegalArgumentException if either {@code aggregateType} or {@code aggregateIds} is null.
   */
  @Query(value = """
    SELECT *
    FROM (
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
            event.extension,
            ROW_NUMBER() OVER (PARTITION BY event.aggregate_id, aggregate.aggregate_type 
                               ORDER BY event.offset_txid DESC, event.offset_id DESC) AS row_num
        FROM ES_EVENT event
        JOIN ES_AGGREGATE aggregate ON aggregate.id = event.aggregate_id
        WHERE aggregate.aggregate_type = :aggregateType
          AND event.aggregate_id IN :aggregateIds
    ) subquery
    WHERE subquery.row_num = 1
    ORDER BY subquery.aggregate_id ASC
    """, nativeQuery = true)
  List<EventEntity> retrieveLatestEventsByAggregateTypeAndAggregateIds(
    @Param("aggregateType") @NotNull String aggregateType,
    @Param("aggregateIds") @NotNull List<UUID> aggregateIds
  );

  /**
   * Retrieves the latest {@link EventEntity} instances for a specific aggregate type, grouped by aggregate ID.
   * This query groups events by aggregate ID and aggregate type and selects the most recent event for each group.
   *
   * @param aggregateType the type of aggregate to retrieve events for. Cannot be null.
   * @return a list of the latest {@link EventEntity} instances for each aggregate ID of the specified type.
   * @throws IllegalArgumentException if {@code aggregateType} is null.
   */
  @Query(value = """
    SELECT *
    FROM (
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
            event.extension,
            ROW_NUMBER() OVER (PARTITION BY event.aggregate_id, aggregate.aggregate_type 
                               ORDER BY event.offset_txid DESC, event.offset_id DESC) AS row_num
        FROM ES_EVENT event
        JOIN ES_AGGREGATE aggregate ON aggregate.id = event.aggregate_id
        WHERE aggregate.aggregate_type = :aggregateType
    ) subquery
    WHERE subquery.row_num = 1
    ORDER BY subquery.aggregate_id ASC
    """, nativeQuery = true)
  List<EventEntity> retrieveLatestEventsByAggregateTypeGroupedByAggregateId(
    @Param("aggregateType") @NotNull String aggregateType
  );

}
