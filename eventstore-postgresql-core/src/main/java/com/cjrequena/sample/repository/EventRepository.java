package com.cjrequena.sample.repository;

import com.cjrequena.sample.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EventRepository extends CrudRepository<EventEntity, UUID> {

}
