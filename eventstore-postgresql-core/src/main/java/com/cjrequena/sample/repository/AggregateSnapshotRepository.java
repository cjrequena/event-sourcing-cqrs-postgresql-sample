package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.AggregateSnapshotEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AggregateSnapshotRepository extends CrudRepository<AggregateSnapshotEntity, UUID> {

}
