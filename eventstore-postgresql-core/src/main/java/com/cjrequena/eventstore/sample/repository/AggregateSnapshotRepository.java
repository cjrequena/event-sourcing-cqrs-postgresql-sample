package com.cjrequena.eventstore.sample.repository;

import com.cjrequena.eventstore.sample.entity.AggregateSnapshotEntity;
import com.cjrequena.eventstore.sample.entity.EventEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AggregateSnapshotRepository extends CrudRepository<AggregateSnapshotEntity, UUID> {

  @Query(value = """
    SELECT *
      FROM es_aggregate_snapshot
     WHERE aggregate_id = :aggregateId
       AND (:aggregateVersion IS NULL OR aggregate_version <= :aggregateVersion)
     ORDER BY aggregate_version DESC
    """, nativeQuery = true)
  List<EventEntity> retrieveAggregateSnapshotByAggregateId(
    @Param("aggregateId") UUID aggregateId,
    @Param("aggregateVersion") @Nullable Long aggregateVersion
  );

}
