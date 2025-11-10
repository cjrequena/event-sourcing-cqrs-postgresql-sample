package com.cjrequena.eventstore.sample.persistence.repository;

import com.cjrequena.eventstore.sample.persistence.entity.AbstractEventEntity;
import com.cjrequena.eventstore.sample.persistence.entity.EventEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
   * Retrieves events for a specific aggregate ID, optionally filtered by aggregate version range.
   * <p>
   * This method uses a native SQL query to fetch events associated with a given {@code aggregateId}.
   * It also allows filtering by aggregate version, where the events' version must be greater than the
   * {@code fromAggregateVersion} and less than or equal to the {@code toAggregateVersion}, if those
   * parameters are provided. If either version parameter is {@code null}, no filtering is applied for that version.
   * <p>
   * The results are ordered by {@code aggregate_version} in ascending order.
   *
   * @param aggregateId the ID of the aggregate whose events are being retrieved; must not be {@code null}
   * @param fromAggregateVersion the lower bound for filtering events by version (exclusive); may be {@code null}
   * @param toAggregateVersion the upper bound for filtering events by version (inclusive); may be {@code null}
   * @return a list of {@link EventEntity} records that match the specified aggregate ID and version range, ordered by version
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
   * Retrieves events of the specified aggregate type that occur after the given offset position,
   * defined by a combination of {@code offsetTxId} and {@code offsetId}.
   * <p>
   * This method uses a native SQL query to return events whose composite offset
   * ({@code offset_txid}, {@code offset_id}) is strictly greater than the provided values.
   * Additionally, it filters out events that are not yet visible according to the current
   * PostgreSQL snapshot (via {@code pg_snapshot_xmin(pg_current_snapshot())}).
   * <p>
   * Results are ordered in ascending order of {@code offset_txid} and {@code offset_id},
   * preserving event commit order.
   *
   * @param aggregateType the aggregate type used to filter events; must not be {@code null}
   * @param offsetTxId the transaction ID after which events should be retrieved; must not be {@code null}
   * @param offsetId the intra-transaction offset ID after which events should be retrieved; must not be {@code null}
   * @return a list of {@link EventEntity} instances that match the criteria, ordered by commit sequence
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
   * Retrieves the latest event for each specified aggregate ID of the given aggregate type.
   * <p>
   * This method uses a native SQL query with a window function to determine
   * the most recent event per aggregate based on descending {@code offset_txid}
   * and {@code offset_id}. Only the latest event for each {@code aggregate_id}
   * in the provided list is returned.
   * <p>
   * The query filters by both {@code aggregate_type} and the specified list of
   * {@code aggregateIds}, and the results are ordered by {@code aggregate_id} ascending.
   *
   * @param aggregateType the type of aggregate to filter events by; must not be {@code null}
   * @param aggregateIds the list of aggregate IDs for which to retrieve the latest events; must not be {@code null}
   * @return a list of the most recent {@link EventEntity} records for each specified aggregate ID
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
   * Retrieves the latest event for a specific aggregate ID and aggregate type.
   * <p>
   * This method uses a native SQL query to return the most recent event for the given {@code aggregateId}
   * and {@code aggregateType}. The latest event is determined by the highest {@code offset_txid} and
   * {@code offset_id}, ensuring that only the most recent event for the specified aggregate is returned.
   * <p>
   * The query uses a window function to partition events by {@code aggregate_id} and {@code aggregate_type},
   * ordering them in descending order by {@code offset_txid} and {@code offset_id}. The event with the highest
   * {@code offset_txid} and {@code offset_id} is selected as the most recent event.
   * <p>
   * The result is ordered by {@code aggregate_id} in ascending order.
   * <p>
   * If no event is found for the given aggregate ID and type, an empty result is returned. In case of multiple
   * results, only the most recent event is selected based on the partitioned sorting.
   *
   * @param aggregateType the type of the aggregate whose latest event is to be retrieved; must not be {@code null}
   * @param aggregateId the ID of the aggregate whose latest event is to be retrieved; must not be {@code null}
   * @return the latest {@link EventEntity} for the specified aggregate ID and aggregate type, or {@code null} if no event is found.
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
          AND event.aggregate_id = :aggregateId
    ) subquery
    WHERE subquery.row_num = 1
    ORDER BY subquery.aggregate_id ASC
    """, nativeQuery = true)
  Optional<EventEntity> retrieveLatestEventByAggregateTypeAndAggregateId(
    @Param("aggregateType") @NotNull String aggregateType,
    @Param("aggregateId") @NotNull UUID aggregateId
  );



  /**
   * Retrieves the latest event for each aggregate of the specified aggregate type.
   * <p>
   * This method uses a native SQL query with a window function to identify
   * the most recent event per aggregate, based on descending order of
   * {@code offset_txid} and {@code offset_id}. Only the latest event for each
   * {@code aggregate_id} is returned.
   * <p>
   * The results are ordered by {@code aggregate_id} in ascending order.
   *
   * @param aggregateType the type of aggregate for which to retrieve latest events; must not be {@code null}
   * @return a list of the most recent {@link EventEntity} records for each aggregate of the given type
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
  List<EventEntity> retrieveLatestEventsByAggregateType(
    @Param("aggregateType") @NotNull String aggregateType
  );

}
