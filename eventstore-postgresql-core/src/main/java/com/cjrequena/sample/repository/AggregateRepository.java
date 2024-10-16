package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.AbstractAggregateEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@Repository
public interface AggregateRepository extends CrudRepository<AbstractAggregateEntity, UUID> {

  @Query(value = "SELECT CASE "
    + " WHEN COUNT(A)> 0 THEN TRUE ELSE FALSE END "
    + " FROM AGGREGATE A "
    + " WHERE A.ID = :id AND A.AGGREGATE_TYPE = :aggregateType",
    nativeQuery = true)
  boolean checkAggregate(@Param("id") UUID id, @Param("aggregateType") String aggregateType);

  @Query(value = "SELECT CASE "
    + " WHEN  VERSION = :#{#aggregate.version} THEN True ELSE False END "
    + " FROM AGGREGATE WHERE ID=:#{#aggregate.id}",
    nativeQuery = true)
  boolean checkVersion(@Param("aggregate") AbstractAggregateEntity abstractAggregateEntity);

}
