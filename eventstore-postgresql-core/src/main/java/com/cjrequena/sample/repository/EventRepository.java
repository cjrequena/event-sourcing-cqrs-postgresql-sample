package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.EventEntity;
import com.cjrequena.sample.entity.MyEventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface EventRepository extends CrudRepository<EventEntity, UUID> {

  @Query(value = """
           SELECT *
             FROM es_event
            WHERE aggregate_id = :aggregateId
              AND (:fromAggregateVersion IS NULL OR aggregate_version > :fromAggregateVersion)
              AND (:toAggregateVersion IS NULL OR aggregate_version <= :toAggregateVersion)
            ORDER BY aggregate_version ASC
           """, nativeQuery = true)
  List<MyEventEntity> retrieveEventsByAggregateId(
    @Param("aggregateId") UUID aggregateId,
    @Param("fromAggregateVersion") Integer fromAggregateVersion,
    @Param("toAggregateVersion") Integer toAggregateVersion
  );
}
