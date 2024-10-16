package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.AbstractEventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EventRepository extends CrudRepository<AbstractEventEntity, UUID> {

}
