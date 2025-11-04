package com.cjrequena.sample.persistence.repository.mongo;

import com.cjrequena.sample.persistence.entity.mongo.MongoAccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface MongoAccountRepository extends ReactiveMongoRepository<MongoAccountEntity, UUID> {

}
