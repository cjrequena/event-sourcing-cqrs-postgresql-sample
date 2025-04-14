package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.AggregateSnapshotEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface AggregateSnapshotRepository extends CrudRepository<AggregateSnapshotEntity, UUID> {

  @Query(value = """
    SELECT *
      FROM es_aggregate_snapshot
     WHERE aggregate_id = :aggregateId
       AND (:aggregateVersion IS NULL OR aggregate_version <= :aggregateVersion)
     ORDER BY aggregate_version DESC
     LIMIT 1
    """, nativeQuery = true)
  AggregateSnapshotEntity retrieveAggregateSnapshot(@Param("aggregateId") UUID aggregateId, @Param("aggregateVersion") @Nullable Long aggregateVersion);

  // Native query to fetch the aggregate snapshot with a limit of 1
  //  @Query(value = """
  //    SELECT s.ID, s.AGGREGATE_ID, a.AGGREGATE_TYPE, s.DATA, s.AGGREGATE_VERSION
  //    FROM ES_AGGREGATE_SNAPSHOT s
  //    JOIN ES_AGGREGATE a ON a.ID = s.AGGREGATE_ID
  //    WHERE s.AGGREGATE_ID = :aggregateId
  //    AND (:aggregateVersion IS NULL OR s.AGGREGATE_VERSION <= :aggregateVersion)
  //    ORDER BY s.AGGREGATE_VERSION DESC
  //    LIMIT 1
  //    """, nativeQuery = true)
  //  Optional<Object[]> retrieveAggregateSnapshot(@Param("aggregateId") UUID aggregateId, @Param("aggregateVersion") Integer aggregateVersion);
}
